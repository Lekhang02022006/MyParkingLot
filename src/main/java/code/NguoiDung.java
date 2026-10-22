package code;

public abstract class NguoiDung {
    protected long ndMa;
    protected String ndHoTen;
    protected String ndEmail;
    protected String ndSoDienThoai;
    protected String ndMatKhau;
    protected String ndTrangThai;

    public NguoiDung() {
    }

    public NguoiDung(long ndMa, String ndHoTen, String ndEmail,
                     String ndSoDienThoai, String ndMatKhau) {
        this.ndMa = ndMa;
        this.ndHoTen = ndHoTen;
        this.ndEmail = ndEmail;
        this.ndSoDienThoai = ndSoDienThoai;
        this.ndMatKhau = ndMatKhau;
    }

    public long getNdMa() {
        return ndMa;
    }

    public void setNdMa(long ndMa) {
        this.ndMa = ndMa;
    }

    public String getNdHoTen() {
        return ndHoTen;
    }

    public void setNdHoTen(String ndHoTen) {
        this.ndHoTen = ndHoTen;
    }

    public String getNdEmail() {
        return ndEmail;
    }

    public void setNdEmail(String ndEmail) {
        this.ndEmail = ndEmail;
    }

    public String getNdSoDienThoai() {
        return ndSoDienThoai;
    }

    public void setNdSoDienThoai(String ndSoDienThoai) {
        this.ndSoDienThoai = ndSoDienThoai;
    }

    public String getNdMatKhau() {
        return ndMatKhau;
    }

    public void setNdMatKhau(String ndMatKhau) {
        this.ndMatKhau = ndMatKhau;
    }

    public String getNdTrangThai() {
        return ndTrangThai;
    }

    public void setNdTrangThai(String ndTrangThai) {
        this.ndTrangThai = ndTrangThai;
    }

    public void hienThiThongTin() {
        System.out.println("Mã người dùng: " + ndMa);
        System.out.println("Họ tên: " + ndHoTen);
        System.out.println("Email: " + ndEmail);
        System.out.println("Số điện thoại: " + ndSoDienThoai);
    }
}