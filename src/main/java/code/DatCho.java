package code;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DatCho {
    public static final BigDecimal TIEN_COC_MAC_DINH =
            new BigDecimal("20000");

    public static final String CHO_THANH_TOAN_COC =
            "CHO_THANH_TOAN_COC";
    public static final String DA_DAT = "DA_DAT";
    public static final String DANG_SU_DUNG = "DANG_SU_DUNG";
    public static final String CHO_THANH_TOAN_PHI =
            "CHO_THANH_TOAN_PHI";
    public static final String DA_HOAN_THANH = "DA_HOAN_THANH";
    public static final String DA_HUY = "DA_HUY";
    public static final String QUA_HAN = "QUA_HAN";

    private long dcMa;
    private long ndMa;
    private long xeMa;
    private long kvMa;
    private Long cdxMa;

    private LocalDateTime dcThoiGianDat;
    private LocalDateTime dcThoiGianVaoDuKien;
    private LocalDateTime dcThoiGianRaDuKien;
    private LocalDateTime dcThoiGianVaoThucTe;
    private LocalDateTime dcThoiGianRaThucTe;

    private BigDecimal dcGiaTheoGio;
    private BigDecimal dcTienCoc;
    private BigDecimal dcTienThanhToan;

    private String dcTrangThai;
    private String dcLyDoHuy;

    public DatCho() {
        this.dcTienCoc = TIEN_COC_MAC_DINH;
        this.dcTrangThai = CHO_THANH_TOAN_COC;
    }

    public long getDcMa() {
        return dcMa;
    }

    public void setDcMa(long dcMa) {
        this.dcMa = dcMa;
    }

    public long getNdMa() {
        return ndMa;
    }

    public void setNdMa(long ndMa) {
        this.ndMa = ndMa;
    }

    public long getXeMa() {
        return xeMa;
    }

    public void setXeMa(long xeMa) {
        this.xeMa = xeMa;
    }

    public long getKvMa() {
        return kvMa;
    }

    public void setKvMa(long kvMa) {
        this.kvMa = kvMa;
    }

    public Long getCdxMa() {
        return cdxMa;
    }

    public void setCdxMa(Long cdxMa) {
        this.cdxMa = cdxMa;
    }

    public LocalDateTime getDcThoiGianDat() {
        return dcThoiGianDat;
    }

    public void setDcThoiGianDat(LocalDateTime value) {
        this.dcThoiGianDat = value;
    }

    public LocalDateTime getDcThoiGianVaoDuKien() {
        return dcThoiGianVaoDuKien;
    }

    public void setDcThoiGianVaoDuKien(LocalDateTime value) {
        this.dcThoiGianVaoDuKien = value;
    }

    public LocalDateTime getDcThoiGianRaDuKien() {
        return dcThoiGianRaDuKien;
    }

    public void setDcThoiGianRaDuKien(LocalDateTime value) {
        this.dcThoiGianRaDuKien = value;
    }

    public LocalDateTime getDcThoiGianVaoThucTe() {
        return dcThoiGianVaoThucTe;
    }

    public void setDcThoiGianVaoThucTe(LocalDateTime value) {
        this.dcThoiGianVaoThucTe = value;
    }

    public LocalDateTime getDcThoiGianRaThucTe() {
        return dcThoiGianRaThucTe;
    }

    public void setDcThoiGianRaThucTe(LocalDateTime value) {
        this.dcThoiGianRaThucTe = value;
    }

    public BigDecimal getDcGiaTheoGio() {
        return dcGiaTheoGio;
    }

    public void setDcGiaTheoGio(BigDecimal value) {
        this.dcGiaTheoGio = value;
    }

    public BigDecimal getDcTienCoc() {
        return dcTienCoc;
    }

    public void setDcTienCoc(BigDecimal value) {
        this.dcTienCoc = value;
    }

    public BigDecimal getDcTienThanhToan() {
        return dcTienThanhToan;
    }

    public void setDcTienThanhToan(BigDecimal value) {
        this.dcTienThanhToan = value;
    }

    public String getDcTrangThai() {
        return dcTrangThai;
    }

    public void setDcTrangThai(String value) {
        this.dcTrangThai = value;
    }

    public String getDcLyDoHuy() {
        return dcLyDoHuy;
    }

    public void setDcLyDoHuy(String value) {
        this.dcLyDoHuy = value;
    }

    public boolean daCheckIn() {
        return DANG_SU_DUNG.equals(dcTrangThai);
    }

    public boolean daCheckOut() {
        return dcThoiGianRaThucTe != null;
    }

    public boolean coTheHuy() {
        return CHO_THANH_TOAN_COC.equals(dcTrangThai)
                || DA_DAT.equals(dcTrangThai);
    }
}
