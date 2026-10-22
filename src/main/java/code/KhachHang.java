package code;

import java.util.ArrayList;
import java.util.List;

public class KhachHang extends NguoiDung {
    private String khDiaChi;
    private List<Xe> danhSachXe;

    public KhachHang() {
        danhSachXe = new ArrayList<>();
    }

    public KhachHang(long ndMa, String hoTen, String email,
                     String soDienThoai, String matKhau,
                     String diaChi) {
        super(ndMa, hoTen, email, soDienThoai, matKhau);
        this.khDiaChi = diaChi;
        this.danhSachXe = new ArrayList<>();
    }

    public String getKhDiaChi() {
        return khDiaChi;
    }

    public void setKhDiaChi(String khDiaChi) {
        this.khDiaChi = khDiaChi;
    }

    public List<Xe> getDanhSachXe() {
        return danhSachXe;
    }

    public void setDanhSachXe(List<Xe> danhSachXe) {
        this.danhSachXe = danhSachXe != null
                ? danhSachXe : new ArrayList<>();
    }

    public void themXe(Xe xe) {
        if (xe != null) {
            danhSachXe.add(xe);
        }
    }

    public void hienThiDanhSachXe() {
        for (Xe xe : danhSachXe) {
            xe.hienThiThongTin();
        }
    }
}