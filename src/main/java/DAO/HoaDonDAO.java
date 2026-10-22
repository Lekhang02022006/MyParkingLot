package DAO;

import java.sql.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class HoaDonDAO extends BaseDAO {

    public Map<String, Object> timTheoMa(long hdMa)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM hoa_don WHERE hd_ma=?", hdMa
        ));
    }

    public Map<String, Object> timTheoDatCho(long dcMa)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM hoa_don WHERE dc_ma=?", dcMa
        ));
    }

    public List<Map<String, Object>> layTatCa()
            throws SQLException {
        return query(
                "SELECT hd.*, dc.nd_ma, dc.xe_ma " +
                        "FROM hoa_don hd JOIN dat_cho dc ON hd.dc_ma=dc.dc_ma " +
                        "ORDER BY hd.hd_ngay_lap DESC"
        );
    }

    public List<Map<String, Object>> layHoaDonChuaThanhToan()
            throws SQLException {
        return query(
                "SELECT * FROM hoa_don " +
                        "WHERE hd_trang_thai IN " +
                        "('CHUA_THANH_TOAN','DANG_THANH_TOAN') " +
                        "ORDER BY hd_ngay_lap"
        );
    }

    /*
     * Làm tròn lên từng giờ.
     * Ví dụ: 2 giờ 10 phút -> 3 giờ.
     */
    public BigDecimal tinhPhiGuiXe(
            LocalDateTime gioVao, LocalDateTime gioRa,
            BigDecimal giaTheoGio) {

        if (gioVao == null || gioRa == null ||
                giaTheoGio == null) {
            throw new IllegalArgumentException(
                    "Thông tin tính phí không hợp lệ.");
        }

        if (gioRa.isBefore(gioVao)) {
            throw new IllegalArgumentException(
                    "Giờ ra không thể trước giờ vào.");
        }

        long giay = Duration.between(gioVao, gioRa).getSeconds();

        long soGio = (giay + 3599) / 3600;

        return giaTheoGio.multiply(BigDecimal.valueOf(soGio))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /*
     * Tạo hóa đơn khi khách check-out.
     * Phải truyền giá đã lưu trong dat_cho, không lấy giá hiện tại.
     */
    public long taoHoaDon(
            long dcMa, LocalDateTime gioVaoThucTe,
            LocalDateTime gioRaThucTe, BigDecimal giaTheoGio,
            BigDecimal tienCoc, String ghiChu)
            throws SQLException {

        BigDecimal tongTien = tinhPhiGuiXe(
                gioVaoThucTe, gioRaThucTe, giaTheoGio
        );

        BigDecimal tienThanhToan = tongTien.subtract(tienCoc);

        if (tienThanhToan.signum() < 0) {
            tienThanhToan = BigDecimal.ZERO;
        }

        BigDecimal tienPhatSinh = tongTien.subtract(tienCoc);

        if (tienPhatSinh.signum() < 0) {
            tienPhatSinh = BigDecimal.ZERO;
        }

        return insertAndGetId(
                "INSERT INTO hoa_don " +
                        "(dc_ma, hd_tong_tien, hd_tien_coc, " +
                        "hd_tien_phat_sinh, hd_tien_thanh_toan, " +
                        "hd_tien_da_thanh_toan, hd_trang_thai, hd_ghi_chu) " +
                        "VALUES (?, ?, ?, ?, ?, 0, 'CHUA_THANH_TOAN', ?) " +
                        "ON CONFLICT (dc_ma) DO NOTHING",
                dcMa, tongTien, tienCoc,
                tienPhatSinh, tienThanhToan, ghiChu
        );
    }

    public boolean capNhatSoTienDaThanhToan(
            long hdMa, BigDecimal soTien)
            throws SQLException {

        if (soTien == null || soTien.signum() < 0) {
            throw new IllegalArgumentException(
                    "Số tiền không hợp lệ.");
        }

        return update(
                "UPDATE hoa_don SET " +
                        "hd_tien_da_thanh_toan=hd_tien_da_thanh_toan+?, " +
                        "hd_trang_thai=CASE " +
                        "WHEN hd_tien_da_thanh_toan+? >= hd_tien_thanh_toan " +
                        "THEN 'DA_THANH_TOAN' " +
                        "ELSE 'DANG_THANH_TOAN' END " +
                        "WHERE hd_ma=? AND " +
                        "hd_tien_da_thanh_toan+? <= hd_tien_thanh_toan",
                soTien, soTien, hdMa, soTien
        ) > 0;
    }

    public boolean capNhatTrangThai(
            long hdMa, String trangThai) throws SQLException {

        if (!Arrays.asList(
                "CHUA_THANH_TOAN", "DANG_THANH_TOAN",
                "DA_THANH_TOAN").contains(trangThai)) {
            throw new IllegalArgumentException(
                    "Trạng thái hóa đơn không hợp lệ.");
        }

        return update(
                "UPDATE hoa_don SET hd_trang_thai=? WHERE hd_ma=?",
                trangThai, hdMa
        ) > 0;
    }

    public boolean capNhatGhiChu(long hdMa, String ghiChu)
            throws SQLException {
        return update(
                "UPDATE hoa_don SET hd_ghi_chu=? WHERE hd_ma=?",
                ghiChu, hdMa
        ) > 0;
    }
}