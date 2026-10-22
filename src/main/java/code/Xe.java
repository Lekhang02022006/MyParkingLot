package code;

public class Xe {
    private long xeMa;
    private String xeBienSo;
    private long ndMa;
    private LoaiXe loaiXe;

    public Xe() {
    }

    public Xe(long xeMa, String xeBienSo,
              long ndMa, LoaiXe loaiXe) {
        this.xeMa = xeMa;
        this.xeBienSo = xeBienSo;
        this.ndMa = ndMa;
        this.loaiXe = loaiXe;
    }

    public long getXeMa() {
        return xeMa;
    }

    public void setXeMa(long xeMa) {
        this.xeMa = xeMa;
    }

    public String getXeBienSo() {
        return xeBienSo;
    }

    public void setXeBienSo(String xeBienSo) {
        this.xeBienSo = xeBienSo;
    }

    public long getNdMa() {
        return ndMa;
    }

    public void setNdMa(long ndMa) {
        this.ndMa = ndMa;
    }

    public LoaiXe getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(LoaiXe loaiXe) {
        this.loaiXe = loaiXe;
    }

    public void hienThiThongTin() {
        System.out.println("Mã xe: " + xeMa);
        System.out.println("Biển số: " + xeBienSo);
        System.out.println("Mã khách hàng: " + ndMa);

        if (loaiXe != null) {
            System.out.println("Loại xe: " + loaiXe.getLxTenLoai());
        }
    }
}