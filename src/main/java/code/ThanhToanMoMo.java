package code;

public class ThanhToanMoMo implements IThanhToan {

    private String ttmmMaGiaoDich;
    private String ttmmSoDienThoai;

    public ThanhToanMoMo() {
    }

    public ThanhToanMoMo(String maGiaoDich, String soDienThoai) {
        this.ttmmMaGiaoDich = maGiaoDich;
        this.ttmmSoDienThoai = soDienThoai;
    }

    public String getTtmmMaGiaoDich() {
        return ttmmMaGiaoDich;
    }

    public void setTtmmMaGiaoDich(String maGiaoDich) {
        this.ttmmMaGiaoDich = maGiaoDich;
    }

    public String getTtmmSoDienThoai() {
        return ttmmSoDienThoai;
    }

    public void setTtmmSoDienThoai(String soDienThoai) {
        this.ttmmSoDienThoai = soDienThoai;
    }

    @Override
    public boolean xuLyThanhToan(double soTien) {
        if (!Double.isFinite(soTien) || soTien <= 0) {
            return false;
        }

        // TODO: Tích hợp API MoMo và xác minh kết quả
        // giao dịch từ máy chủ MoMo.
        System.out.println("Khởi tạo thanh toán MoMo: "
                + soTien + " VNĐ");

        return false;
    }
}