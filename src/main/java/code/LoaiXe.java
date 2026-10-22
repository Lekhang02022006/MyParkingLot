package code;

import java.math.BigDecimal;

public class LoaiXe {
    private long lxMa;
    private String lxTenLoai;
    private BigDecimal lxGiaTheoGio;
    private String lxTrangThai;

    public LoaiXe() {
    }

    public LoaiXe(long lxMa, String lxTenLoai,
                  BigDecimal lxGiaTheoGio) {
        this.lxMa = lxMa;
        this.lxTenLoai = lxTenLoai;
        this.lxGiaTheoGio = lxGiaTheoGio;
    }

    public long getLxMa() {
        return lxMa;
    }

    public void setLxMa(long lxMa) {
        this.lxMa = lxMa;
    }

    public String getLxTenLoai() {
        return lxTenLoai;
    }

    public void setLxTenLoai(String lxTenLoai) {
        this.lxTenLoai = lxTenLoai;
    }

    public BigDecimal getLxGiaTheoGio() {
        return lxGiaTheoGio;
    }

    public void setLxGiaTheoGio(BigDecimal lxGiaTheoGio) {
        this.lxGiaTheoGio = lxGiaTheoGio;
    }

    public String getLxTrangThai() {
        return lxTrangThai;
    }

    public void setLxTrangThai(String lxTrangThai) {
        this.lxTrangThai = lxTrangThai;
    }

    public void hienThiThongTin() {
        System.out.println("Mã loại xe: " + lxMa);
        System.out.println("Tên loại xe: " + lxTenLoai);
        System.out.println("Giá theo giờ: " + lxGiaTheoGio + " VNĐ");
    }
}