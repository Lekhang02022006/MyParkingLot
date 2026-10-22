package code;

import java.math.BigDecimal;

public interface IThanhToan {

    boolean xuLyThanhToan(double soTien);

    default boolean xuLyThanhToan(BigDecimal soTien) {
        if (soTien == null) {
            return false;
        }

        return xuLyThanhToan(soTien.doubleValue());
    }
}