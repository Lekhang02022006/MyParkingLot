package code;

public class ThanhToanVNPay implements IThanhToan {

    private String ttvnpMaGiaoDich;
    private String ttvnpMaThamChieu;

    public ThanhToanVNPay() {
    }

    public ThanhToanVNPay(String maGiaoDich, String maThamChieu) {
        this.ttvnpMaGiaoDich = maGiaoDich;
        this.ttvnpMaThamChieu = maThamChieu;
    }

    public String getTtvnpMaGiaoDich() {
        return ttvnpMaGiaoDich;
    }

    public void setTtvnpMaGiaoDich(String maGiaoDich) {
        this.ttvnpMaGiaoDich = maGiaoDich;
    }

    public String getTtvnpMaThamChieu() {
        return ttvnpMaThamChieu;
    }

    public void setTtvnpMaThamChieu(String maThamChieu) {
        this.ttvnpMaThamChieu = maThamChieu;
    }

    @Override
    public boolean xuLyThanhToan(double soTien) {
        if (!Double.isFinite(soTien) || soTien <= 0) {
            return false;
        }

        // TODO: Tích hợp API VNPay và xác minh chữ ký
        // cùng trạng thái giao dịch từ máy chủ VNPay.
        System.out.println("Khởi tạo thanh toán VNPay: "
                + soTien + " VNĐ");

        return false;
    }
}