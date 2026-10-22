package DAO;

import config.DBConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class DatChoDAO extends BaseDAO {

    public Map<String, Object> timTheoMa(long ma)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM dat_cho WHERE dc_ma=?", ma
        ));
    }

    public List<Map<String, Object>> layTheoKhachHang(long ndMa)
            throws SQLException {
        return query(
                "SELECT dc.*, x.xe_bien_so, kv.kv_ten_khu_vuc " +
                        "FROM dat_cho dc " +
                        "JOIN xe x ON dc.xe_ma=x.xe_ma " +
                        "JOIN khu_vuc kv ON dc.kv_ma=kv.kv_ma " +
                        "WHERE dc.nd_ma=? ORDER BY dc.dc_thoi_gian_dat DESC",
                ndMa
        );
    }

    public List<Map<String, Object>> layTheoKhuVuc(long kvMa)
            throws SQLException {
        return query(
                "SELECT * FROM dat_cho WHERE kv_ma=? " +
                        "ORDER BY dc_thoi_gian_vao_du_kien",
                kvMa
        );
    }

    public List<Map<String, Object>> layTheoTrangThai(
            String trangThai) throws SQLException {
        return query(
                "SELECT * FROM dat_cho WHERE dc_trang_thai=? " +
                        "ORDER BY dc_thoi_gian_dat",
                trangThai
        );
    }

    public long taoDatCho(
            long ndMa, long xeMa, long kvMa,
            LocalDateTime gioVao, LocalDateTime gioRa,
            BigDecimal giaTheoGio) throws SQLException {

        LocalDateTime now = LocalDateTime.now();

        if (gioVao == null || gioRa == null ||
                giaTheoGio == null || giaTheoGio.signum() < 0) {
            throw new IllegalArgumentException(
                    "Thông tin đặt chỗ không hợp lệ.");
        }

        if (!gioRa.isAfter(gioVao)) {
            throw new IllegalArgumentException(
                    "Giờ ra phải sau giờ vào.");
        }

        long phutTruocGioVao =
                java.time.Duration.between(now, gioVao).toMinutes();

        if (phutTruocGioVao < 15 || phutTruocGioVao > 720) {
            throw new IllegalArgumentException(
                    "Chỉ được đặt trước từ 15 phút đến 12 giờ.");
        }

        // Xác minh xe thuộc khách, loại xe khớp khu vực,
        // và các đối tượng liên quan đang hoạt động.
        List<Map<String, Object>> hopLe = query(
                "SELECT x.xe_ma " +
                        "FROM xe x " +
                        "JOIN loai_xe lx ON lx.lx_ma=x.lx_ma " +
                        "JOIN khu_vuc kv ON kv.lx_ma=lx.lx_ma " +
                        "WHERE x.xe_ma=? AND x.nd_ma=? AND kv.kv_ma=? " +
                        "AND lx.lx_trang_thai='HOAT_DONG' " +
                        "AND kv.kv_trang_thai='HOAT_DONG'",
                xeMa, ndMa, kvMa
        );

        if (hopLe.isEmpty()) {
            throw new IllegalArgumentException(
                    "Xe không thuộc khách hoặc loại xe không phù hợp khu vực.");
        }

        // Giá được truyền vào phải là giá đã đọc tại thời điểm đặt.
        return insertAndGetId(
                "INSERT INTO dat_cho " +
                        "(nd_ma, xe_ma, kv_ma, dc_thoi_gian_vao_du_kien, " +
                        "dc_thoi_gian_ra_du_kien, dc_gia_theo_gio, " +
                        "dc_tien_coc, dc_trang_thai) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 20000, 'CHO_THANH_TOAN_COC')",
                ndMa, xeMa, kvMa, gioVao, gioRa, giaTheoGio
        );
    }

    /*
     * Chỉ gọi sau khi đã xác minh giao dịch đặt cọc thành công
     * từ cổng thanh toán.
     *
     * Khóa dòng khu vực để tuần tự hóa các yêu cầu xác nhận
     * cùng khu vực. Việc kiểm tra sức chứa phải tính cả các
     * đơn đã xác nhận có khoảng thời gian chồng lấn.
     */
    public boolean xacNhanSauThanhToan(long dcMa)
            throws SQLException {

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                long kvMa;
                Timestamp vao;
                Timestamp ra;
                String trangThai;

                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT kv_ma, dc_thoi_gian_vao_du_kien, " +
                                "dc_thoi_gian_ra_du_kien, dc_trang_thai " +
                                "FROM dat_cho WHERE dc_ma=? FOR UPDATE")) {

                    ps.setLong(1, dcMa);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        kvMa = rs.getLong("kv_ma");
                        vao = rs.getTimestamp("dc_thoi_gian_vao_du_kien");
                        ra = rs.getTimestamp("dc_thoi_gian_ra_du_kien");
                        trangThai = rs.getString("dc_trang_thai");
                    }
                }

                if (!"CHO_THANH_TOAN_COC".equals(trangThai)) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT kv_ma FROM khu_vuc " +
                                "WHERE kv_ma=? FOR UPDATE")) {
                    ps.setLong(1, kvMa);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                // Chỉ tính các đơn đang chiếm hoặc giữ sức chứa.
                long sucChua;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM cho_dau_xe " +
                                "WHERE kv_ma=? AND cdx_trang_thai <> 'BAO_TRI'")) {

                    ps.setLong(1, kvMa);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        sucChua = rs.getLong(1);
                    }
                }

                long donTrung;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM dat_cho " +
                                "WHERE kv_ma=? AND dc_ma<>? " +
                                "AND dc_trang_thai IN " +
                                "('DA_DAT','DANG_SU_DUNG','CHO_THANH_TOAN_PHI') " +
                                "AND dc_thoi_gian_vao_du_kien < ? " +
                                "AND dc_thoi_gian_ra_du_kien > ?")) {

                    ps.setLong(1, kvMa);
                    ps.setLong(2, dcMa);
                    ps.setTimestamp(3, ra);
                    ps.setTimestamp(4, vao);

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        donTrung = rs.getLong(1);
                    }
                }

                if (donTrung >= sucChua) {
                    // Đơn không được xác nhận vì hết sức chứa.
                    // Việc hoàn cọc phải được tạo thành giao dịch
                    // HOAN_TIEN riêng và xử lý idempotent.
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE dat_cho SET dc_trang_thai='DA_HUY', " +
                                    "dc_ly_do_huy='Khu vực đã hết chỗ' " +
                                    "WHERE dc_ma=? " +
                                    "AND dc_trang_thai='CHO_THANH_TOAN_COC'")) {

                        ps.setLong(1, dcMa);
                        ps.executeUpdate();
                    }

                    conn.commit();
                    return false;
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE dat_cho SET dc_trang_thai='DA_DAT' " +
                                "WHERE dc_ma=? " +
                                "AND dc_trang_thai='CHO_THANH_TOAN_COC'")) {

                    ps.setLong(1, dcMa);
                    boolean ok = ps.executeUpdate() == 1;

                    if (!ok) {
                        conn.rollback();
                        return false;
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /*
     * Check-in: gán chỗ trống đầu tiên.
     * Nên gọi phương thức này trong transaction cùng với
     * việc khóa đơn và khu vực để tránh gán trùng chỗ.
     */
    public boolean checkIn(long dcMa) throws SQLException {

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                long kvMa;
                String trangThai;
                Timestamp gioVao;

                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT kv_ma, dc_trang_thai, " +
                                "dc_thoi_gian_vao_du_kien " +
                                "FROM dat_cho WHERE dc_ma=? FOR UPDATE")) {

                    ps.setLong(1, dcMa);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        kvMa = rs.getLong("kv_ma");
                        trangThai = rs.getString("dc_trang_thai");
                        gioVao = rs.getTimestamp("dc_thoi_gian_vao_du_kien");
                    }
                }

                if (!"DA_DAT".equals(trangThai)) {
                    conn.rollback();
                    return false;
                }

                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                if (now.before(gioVao) ||
                        now.after(Timestamp.valueOf(
                                gioVao.toLocalDateTime().plusMinutes(15)))) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT kv_ma FROM khu_vuc " +
                                "WHERE kv_ma=? FOR UPDATE")) {
                    ps.setLong(1, kvMa);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                long cdxMa;

                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT cdx_ma FROM cho_dau_xe " +
                                "WHERE kv_ma=? AND cdx_trang_thai='TRONG' " +
                                "ORDER BY cdx_ma_cho LIMIT 1 FOR UPDATE")) {

                    ps.setLong(1, kvMa);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                        cdxMa = rs.getLong("cdx_ma");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE cho_dau_xe SET cdx_trang_thai='DANG_CO_XE' " +
                                "WHERE cdx_ma=? AND cdx_trang_thai='TRONG'")) {

                    ps.setLong(1, cdxMa);

                    if (ps.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE dat_cho SET cdx_ma=?, " +
                                "dc_thoi_gian_vao_thuc_te=?, " +
                                "dc_trang_thai='DANG_SU_DUNG' WHERE dc_ma=?")) {

                    ps.setLong(1, cdxMa);
                    ps.setTimestamp(2, now);
                    ps.setLong(3, dcMa);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO lich_su_cho_dau (dc_ma, cdx_ma) " +
                                "VALUES (?, ?)")) {

                    ps.setLong(1, dcMa);
                    ps.setLong(2, cdxMa);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /*
     * Check-out: ghi nhận giờ ra và giải phóng chỗ ngay.
     * Hóa đơn cần được tính từ thời gian thực tế, không phải
     * thời gian ra dự kiến.
     */
    public boolean checkOut(long dcMa) throws SQLException {

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                long cdxMa;

                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT cdx_ma FROM dat_cho " +
                                "WHERE dc_ma=? AND dc_trang_thai='DANG_SU_DUNG' " +
                                "FOR UPDATE")) {

                    ps.setLong(1, dcMa);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                        cdxMa = rs.getLong("cdx_ma");
                    }
                }

                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE dat_cho SET dc_thoi_gian_ra_thuc_te=?, " +
                                "dc_trang_thai='CHO_THANH_TOAN_PHI' " +
                                "WHERE dc_ma=? AND dc_trang_thai='DANG_SU_DUNG'")) {

                    ps.setTimestamp(1, now);
                    ps.setLong(2, dcMa);

                    if (ps.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE cho_dau_xe SET cdx_trang_thai='TRONG' " +
                                "WHERE cdx_ma=? AND cdx_trang_thai='DANG_CO_XE'")) {

                    ps.setLong(1, cdxMa);

                    if (ps.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE lich_su_cho_dau SET " +
                                "lscd_thoi_gian_giai_phong=? " +
                                "WHERE dc_ma=? AND cdx_ma=? " +
                                "AND lscd_thoi_gian_giai_phong IS NULL")) {

                    ps.setTimestamp(1, now);
                    ps.setLong(2, dcMa);
                    ps.setLong(3, cdxMa);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public boolean huyDatCho(long dcMa, String lyDo)
            throws SQLException {
        return update(
                "UPDATE dat_cho SET dc_trang_thai='DA_HUY', " +
                        "dc_ly_do_huy=? WHERE dc_ma=? " +
                        "AND dc_trang_thai IN ('CHO_THANH_TOAN_COC','DA_DAT')",
                lyDo, dcMa
        ) > 0;
    }

    public boolean capNhatTrangThai(
            long dcMa, String trangThai) throws SQLException {
        return update(
                "UPDATE dat_cho SET dc_trang_thai=? WHERE dc_ma=?",
                trangThai, dcMa
        ) > 0;
    }
}