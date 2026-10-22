
import config.DBConnection;

public class TestDBConnection {

    public static void main(String[] args) {
        if (DBConnection.testConnection()) {
            System.out.println("Kết nối Supabase thành công!");
        } else {
            System.out.println("Kết nối Supabase thất bại!");
        }
    }
}
