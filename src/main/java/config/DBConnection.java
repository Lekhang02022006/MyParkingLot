
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    // Đọc thông tin kết nối từ biến môi trường.
    // Không ghi mật khẩu database trực tiếp vào mã nguồn.
    private static final String URL =
            System.getenv("SUPABASE_DB_URL");

    private static final String USER =
            System.getenv("SUPABASE_DB_USER");

    private static final String PASSWORD =
            System.getenv("SUPABASE_DB_PASSWORD");

    private DBConnection() {
        // Không cho phép khởi tạo đối tượng DBConnection.
    }

    /**
     * Mở một kết nối mới đến PostgreSQL.
     * Mỗi lần gọi, phương thức trả về một Connection riêng.
     * Người gọi có trách nhiệm đóng Connection sau khi sử dụng.
     */
    public static Connection getConnection() throws SQLException {
        if (URL == null || URL.isBlank()
                || USER == null || USER.isBlank()
                || PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "Chưa cấu hình SUPABASE_DB_URL, "
                            + "SUPABASE_DB_USER hoặc SUPABASE_DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Kiểm tra kết nối database.
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            System.err.println(
                    "Kết nối Supabase thất bại: " + e.getMessage()
            );
            return false;
        }
    }
}
