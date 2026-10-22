package DAO;

import java.sql.SQLException;
import java.util.*;

public class XeDAO extends BaseDAO {

    public Map<String, Object> timTheoMa(long ma)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM xe WHERE xe_ma = ?", ma
        ));
    }

    public Map<String, Object> timTheoBienSo(String bienSo)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM xe WHERE UPPER(xe_bien_so) = UPPER(?)",
                bienSo
        ));
    }

    public List<Map<String, Object>> layTheoKhachHang(long ndMa)
            throws SQLException {
        return query(
                "SELECT x.*, lx.lx_ten_loai, lx.lx_gia_theo_gio " +
                        "FROM xe x JOIN loai_xe lx ON x.lx_ma = lx.lx_ma " +
                        "WHERE x.nd_ma = ? ORDER BY x.xe_ma",
                ndMa
        );
    }

    public List<Map<String, Object>> layTatCa()
            throws SQLException {
        return query(
                "SELECT x.*, lx.lx_ten_loai " +
                        "FROM xe x JOIN loai_xe lx ON x.lx_ma = lx.lx_ma " +
                        "ORDER BY x.xe_ma"
        );
    }

    public long themXe(
            String bienSo, long ndMa, long lxMa, String moTa)
            throws SQLException {
        return insertAndGetId(
                "INSERT INTO xe (xe_bien_so, nd_ma, lx_ma, xe_mo_ta) " +
                        "VALUES (?, ?, ?, ?)",
                bienSo, ndMa, lxMa, moTa
        );
    }

    public boolean capNhatXe(
            long xeMa, String bienSo, long lxMa, String moTa)
            throws SQLException {
        return update(
                "UPDATE xe SET xe_bien_so=?, lx_ma=?, xe_mo_ta=? " +
                        "WHERE xe_ma=?",
                bienSo, lxMa, moTa, xeMa
        ) > 0;
    }

    public boolean xoaXe(long xeMa) throws SQLException {
        return update(
                "DELETE FROM xe WHERE xe_ma=?", xeMa
        ) > 0;
    }

    public boolean laXeCuaKhach(long xeMa, long ndMa)
            throws SQLException {
        return !query(
                "SELECT xe_ma FROM xe WHERE xe_ma=? AND nd_ma=?",
                xeMa, ndMa
        ).isEmpty();
    }
}