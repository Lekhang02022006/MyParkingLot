package code;

public class ChoDauXe {
    private long cdxMa;
    private String cdxMaCho;
    private String cdxTrangThai;
    private long kvMa;
    private Long dcMa;

    public ChoDauXe() {
    }

    public ChoDauXe(long cdxMa, String cdxMaCho,
                    String cdxTrangThai, long kvMa) {
        this.cdxMa = cdxMa;
        this.cdxMaCho = cdxMaCho;
        this.cdxTrangThai = cdxTrangThai;
        this.kvMa = kvMa;
    }

    public long getCdxMa() {
        return cdxMa;
    }

    public void setCdxMa(long cdxMa) {
        this.cdxMa = cdxMa;
    }

    public String getCdxMaCho() {
        return cdxMaCho;
    }

    public void setCdxMaCho(String cdxMaCho) {
        this.cdxMaCho = cdxMaCho;
    }

    public String getCdxTrangThai() {
        return cdxTrangThai;
    }

    public void setCdxTrangThai(String cdxTrangThai) {
        this.cdxTrangThai = cdxTrangThai;
    }

    public long getKvMa() {
        return kvMa;
    }

    public void setKvMa(long kvMa) {
        this.kvMa = kvMa;
    }

    public Long getDcMa() {
        return dcMa;
    }

    public void setDcMa(Long dcMa) {
        this.dcMa = dcMa;
    }

    public boolean laChoTrong() {
        return "TRONG".equals(cdxTrangThai);
    }
}