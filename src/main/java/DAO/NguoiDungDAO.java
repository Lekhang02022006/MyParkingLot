package DAO;

import config.DBConnection;
import java.sql.*;
import java.util.*;

public class NguoiDungDAO extends BaseDAO {

    public Map<String, Object> timTheoMa(long ma)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM nguoi_dung WHERE nd_ma = ?", ma
        ));
    }

    public Map<String, Object> timTheoEmail(String email)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM nguoi_dung WHERE nd_email = ?", email
        ));
    }

    public Map<String, Object> timTheoSoDienThoai(String sdt)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM nguoi_dung WHERE nd_so_dien_thoai = ?", sdt
        ));
    }

    public List<Map<String, Object>> layTatCa()
            throws SQLException {
        return query("SELECT * FROM nguoi_dung ORDER BY nd_ma");
    }

    public long taoTaiKhoanKhachHang(
            String hoTen, String email, String sdt,
            String matKhau, String diaChi) throws SQLException {

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                long ma;

                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO nguoi_dung " +
                                "(nd_ho_ten, nd_email, nd_so_dien_thoai, nd_mat_khau) " +
                                "VALUES (?, ?, ?, ?) RETURNING nd_ma")) {

                    ps.setString(1, hoTen);
                    ps.setString(2, email);
                    ps.setString(3, sdt);
                    ps.setString(4, matKhau);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Không tạo được tài khoản.");
                        }
                        ma = rs.getLong("nd_ma");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO khach_hang (nd_ma, kh_dia_chi) " +
                                "VALUES (?, ?)")) {

                    ps.setLong(1, ma);
                    ps.setString(2, diaChi);
                    ps.executeUpdate();
                }

                conn.commit();
                return ma;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public boolean capNhatThongTin(
            long ma, String hoTen, String email,
            String sdt, String trangThai) throws SQLException {

        return update(
                "UPDATE nguoi_dung SET nd_ho_ten=?, nd_email=?, " +
                        "nd_so_dien_thoai=?, nd_trang_thai=? WHERE nd_ma=?",
                hoTen, email, sdt, trangThai, ma
        ) > 0;
    }

    public boolean capNhatDiaChi(
            long ma, String diaChi) throws SQLException {
        return update(
                "UPDATE khach_hang SET kh_dia_chi=? WHERE nd_ma=?",
                diaChi, ma
        ) > 0;
    }

    public boolean capNhatMatKhau(
            long ma, String matKhauDaHash) throws SQLException {
        return update(
                "UPDATE nguoi_dung SET nd_mat_khau=? WHERE nd_ma=?",
                matKhauDaHash, ma
        ) > 0;
    }

    public boolean capNhatTrangThai(
            long ma, String trangThai) throws SQLException {
        return update(
                "UPDATE nguoi_dung SET nd_trang_thai=? WHERE nd_ma=?",
                trangThai, ma
        ) > 0;
    }
}