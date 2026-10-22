package code;

public class KhuVuc {
    private long kvMa;
    private String kvTenKhuVuc;
    private int kvTongSoCho;
    private long lxMa;
    private String kvTrangThai;

    public KhuVuc() {
    }

    public KhuVuc(long kvMa, String kvTenKhuVuc,
                  int kvTongSoCho, long lxMa) {
        this.kvMa = kvMa;
        this.kvTenKhuVuc = kvTenKhuVuc;
        this.kvTongSoCho = kvTongSoCho;
        this.lxMa = lxMa;
    }

    public long getKvMa() {
        return kvMa;
    }

    public void setKvMa(long kvMa) {
        this.kvMa = kvMa;
    }

    public String getKvTenKhuVuc() {
        return kvTenKhuVuc;
    }

    public void setKvTenKhuVuc(String kvTenKhuVuc) {
        this.kvTenKhuVuc = kvTenKhuVuc;
    }

    public int getKvTongSoCho() {
        return kvTongSoCho;
    }

    public void setKvTongSoCho(int kvTongSoCho) {
        this.kvTongSoCho = kvTongSoCho;
    }

    public long getLxMa() {
        return lxMa;
    }

    public void setLxMa(long lxMa) {
        this.lxMa = lxMa;
    }

    public String getKvTrangThai() {
        return kvTrangThai;
    }

    public void setKvTrangThai(String kvTrangThai) {
        this.kvTrangThai = kvTrangThai;
    }
}