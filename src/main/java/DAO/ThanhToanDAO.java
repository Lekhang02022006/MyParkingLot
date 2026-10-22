package DAO;

import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.*;

public class ThanhToanDAO extends BaseDAO {

    public Map<String, Object> timTheoMa(long ma)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM thanh_toan WHERE tt_ma=?", ma
        ));
    }

    public List<Map<String, Object>> layTheoDatCho(long dcMa)
            throws SQLException {
        return query(
                "SELECT * FROM thanh_toan WHERE dc_ma=? " +
                        "ORDER BY tt_thoi_gian_tao DESC",
                dcMa
        );
    }

    public Map<String, Object> timTheoIdempotency(
            String khoa) throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM thanh_toan WHERE tt_khoa_idempotency=?",
                khoa
        ));
    }

    public Map<String, Object> timTheoGiaoDichCong(
            String phuongThuc, String maGiaoDich)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM thanh_toan " +
                        "WHERE tt_phuong_thuc=? AND tt_ma_giao_dich_cong=?",
                phuongThuc, maGiaoDich
        ));
    }

    public long taoGiaoDich(
            long dcMa, String loai, String phuongThuc,
            BigDecimal soTien, String khoaIdempotency,
            Long giaoDichGoc, String ghiChu)
            throws SQLException {

        if (soTien == null || soTien.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Số tiền giao dịch phải lớn hơn 0.");
        }

        if (!Arrays.asList(
                        "DAT_COC", "THANH_TOAN_PHI", "HOAN_TIEN")
                .contains(loai)) {
            throw new IllegalArgumentException(
                    "Loại giao dịch không hợp lệ.");
        }

        if (!Arrays.asList("MOMO", "VNPAY").contains(phuongThuc)) {
            throw new IllegalArgumentException(
                    "Phương thức thanh toán không hợp lệ.");
        }

        if (khoaIdempotency == null ||
                khoaIdempotency.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Khóa idempotency không được để trống.");
        }

        return insertAndGetId(
                "INSERT INTO thanh_toan " +
                        "(dc_ma, tt_loai, tt_phuong_thuc, tt_so_tien, " +
                        "tt_khoa_idempotency, tt_giao_dich_goc, tt_ghi_chu) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                dcMa, loai, phuongThuc, soTien,
                khoaIdempotency, giaoDichGoc, ghiChu
        );
    }

    public boolean capNhatKetQua(
            long ttMa, String trangThai,
            String maGiaoDichCong, String ghiChu)
            throws SQLException {

        if (!Arrays.asList(
                        "CHO_XU_LY", "THANH_CONG", "THAT_BAI",
                        "DANG_HOAN_TIEN", "DA_HOAN_TIEN")
                .contains(trangThai)) {
            throw new IllegalArgumentException(
                    "Trạng thái thanh toán không hợp lệ.");
        }

        return update(
                "UPDATE thanh_toan SET tt_trang_thai=?, " +
                        "tt_ma_giao_dich_cong=?, tt_ghi_chu=?, " +
                        "tt_thoi_gian_xu_ly=CURRENT_TIMESTAMP " +
                        "WHERE tt_ma=? AND tt_trang_thai IN " +
                        "('CHO_XU_LY','THAT_BAI','DANG_HOAN_TIEN')",
                trangThai, maGiaoDichCong, ghiChu, ttMa
        ) > 0;
    }

    public boolean danhDauHoanTienDangXuLy(long ttMa)
            throws SQLException {
        return update(
                "UPDATE thanh_toan SET tt_trang_thai='DANG_HOAN_TIEN' " +
                        "WHERE tt_ma=? AND tt_loai='HOAN_TIEN' " +
                        "AND tt_trang_thai IN ('CHO_XU_LY','THAT_BAI')",
                ttMa
        ) > 0;
    }

    public boolean danhDauDaHoanTien(long ttMa)
            throws SQLException {
        return update(
                "UPDATE thanh_toan SET tt_trang_thai='DA_HOAN_TIEN', " +
                        "tt_thoi_gian_xu_ly=CURRENT_TIMESTAMP " +
                        "WHERE tt_ma=? AND tt_loai='HOAN_TIEN' " +
                        "AND tt_trang_thai='DANG_HOAN_TIEN'",
                ttMa
        ) > 0;
    }

    public BigDecimal tongTienThanhToanThanhCong(long dcMa)
            throws SQLException {

        Map<String, Object> row = firstOrNull(query(
                "SELECT COALESCE(SUM(tt_so_tien), 0) AS tong " +
                        "FROM thanh_toan WHERE dc_ma=? " +
                        "AND tt_loai IN ('DAT_COC','THANH_TOAN_PHI') " +
                        "AND tt_trang_thai='THANH_CONG'",
                dcMa
        ));

        return (BigDecimal) row.get("tong");
    }

    public List<Map<String, Object>> layGiaoDichCanDoiSoat()
            throws SQLException {
        return query(
                "SELECT * FROM thanh_toan " +
                        "WHERE tt_trang_thai IN " +
                        "('CHO_XU_LY','DANG_HOAN_TIEN') " +
                        "ORDER BY tt_thoi_gian_tao"
        );
    }
}