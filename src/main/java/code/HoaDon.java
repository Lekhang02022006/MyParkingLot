package code;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDon {
    public static final String CHUA_THANH_TOAN = "CHUA_THANH_TOAN";
    public static final String DANG_THANH_TOAN = "DANG_THANH_TOAN";
    public static final String DA_THANH_TOAN = "DA_THANH_TOAN";

    private long hdMa;
    private long dcMa;
    private LocalDateTime hdNgayLap;

    private BigDecimal hdTongTien;
    private BigDecimal hdTienCoc;
    private BigDecimal hdTienPhatSinh;
    private BigDecimal hdTienThanhToan;
    private BigDecimal hdTienDaThanhToan;

    private String hdTrangThai;
    private String hdGhiChu;

    public HoaDon() {
        this.hdTongTien = BigDecimal.ZERO;
        this.hdTienCoc = BigDecimal.ZERO;
        this.hdTienPhatSinh = BigDecimal.ZERO;
        this.hdTienThanhToan = BigDecimal.ZERO;
        this.hdTienDaThanhToan = BigDecimal.ZERO;
        this.hdTrangThai = CHUA_THANH_TOAN;
    }

    public long getHdMa() {
        return hdMa;
    }

    public void setHdMa(long hdMa) {
        this.hdMa = hdMa;
    }

    public long getDcMa() {
        return dcMa;
    }

    public void setDcMa(long dcMa) {
        this.dcMa = dcMa;
    }

    public LocalDateTime getHdNgayLap() {
        return hdNgayLap;
    }

    public void setHdNgayLap(LocalDateTime hdNgayLap) {
        this.hdNgayLap = hdNgayLap;
    }

    public BigDecimal getHdTongTien() {
        return hdTongTien;
    }

    public void setHdTongTien(BigDecimal hdTongTien) {
        this.hdTongTien = hdTongTien;
    }

    public BigDecimal getHdTienCoc() {
        return hdTienCoc;
    }

    public void setHdTienCoc(BigDecimal hdTienCoc) {
        this.hdTienCoc = hdTienCoc;
    }

    public BigDecimal getHdTienPhatSinh() {
        return hdTienPhatSinh;
    }

    public void setHdTienPhatSinh(BigDecimal hdTienPhatSinh) {
        this.hdTienPhatSinh = hdTienPhatSinh;
    }

    public BigDecimal getHdTienThanhToan() {
        return hdTienThanhToan;
    }

    public void setHdTienThanhToan(BigDecimal hdTienThanhToan) {
        this.hdTienThanhToan = hdTienThanhToan;
    }

    public BigDecimal getHdTienDaThanhToan() {
        return hdTienDaThanhToan;
    }

    public void setHdTienDaThanhToan(BigDecimal hdTienDaThanhToan) {
        this.hdTienDaThanhToan = hdTienDaThanhToan;
    }

    public String getHdTrangThai() {
        return hdTrangThai;
    }

    public void setHdTrangThai(String hdTrangThai) {
        this.hdTrangThai = hdTrangThai;
    }

    public String getHdGhiChu() {
        return hdGhiChu;
    }

    public void setHdGhiChu(String hdGhiChu) {
        this.hdGhiChu = hdGhiChu;
    }

    public BigDecimal tinhSoTienConPhaiTra() {
        BigDecimal tong = hdTienThanhToan != null
                ? hdTienThanhToan : BigDecimal.ZERO;

        BigDecimal daTra = hdTienDaThanhToan != null
                ? hdTienDaThanhToan : BigDecimal.ZERO;

        BigDecimal conLai = tong.subtract(daTra);

        return conLai.max(BigDecimal.ZERO);
    }

    public boolean daThanhToanDu() {
        return tinhSoTienConPhaiTra().compareTo(BigDecimal.ZERO) == 0;
    }
}