package code;

public class NhanVien extends NguoiDung {
    private String nvChucVu;
    private String nvCaLamViec;

    public NhanVien() {
    }

    public NhanVien(long ndMa, String hoTen, String email,
                    String soDienThoai, String matKhau,
                    String chucVu, String caLamViec) {
        super(ndMa, hoTen, email, soDienThoai, matKhau);
        this.nvChucVu = chucVu;
        this.nvCaLamViec = caLamViec;
    }

    public String getNvChucVu() {
        return nvChucVu;
    }

    public void setNvChucVu(String nvChucVu) {
        this.nvChucVu = nvChucVu;
    }

    public String getNvCaLamViec() {
        return nvCaLamViec;
    }

    public void setNvCaLamViec(String nvCaLamViec) {
        this.nvCaLamViec = nvCaLamViec;
    }
}