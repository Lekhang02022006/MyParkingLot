package DAO;

import java.sql.SQLException;
import java.util.*;

public class KhuVucDAO extends BaseDAO {

    public Map<String, Object> timTheoMa(long ma)
            throws SQLException {
        return firstOrNull(query(
                "SELECT kv.*, lx.lx_ten_loai " +
                        "FROM khu_vuc kv JOIN loai_xe lx ON kv.lx_ma=lx.lx_ma " +
                        "WHERE kv.kv_ma=?",
                ma
        ));
    }

    public List<Map<String, Object>> layTatCa()
            throws SQLException {
        return query(
                "SELECT kv.*, lx.lx_ten_loai " +
                        "FROM khu_vuc kv JOIN loai_xe lx ON kv.lx_ma=lx.lx_ma " +
                        "ORDER BY kv.kv_ma"
        );
    }

    public List<Map<String, Object>> layTheoLoaiXe(long lxMa)
            throws SQLException {
        return query(
                "SELECT * FROM khu_vuc " +
                        "WHERE lx_ma=? AND kv_trang_thai='HOAT_DONG' " +
                        "ORDER BY kv_ma",
                lxMa
        );
    }

    public long themKhuVuc(
            String ten, long lxMa, String moTa)
            throws SQLException {
        return insertAndGetId(
                "INSERT INTO khu_vuc " +
                        "(kv_ten_khu_vuc, lx_ma, kv_mo_ta) VALUES (?, ?, ?)",
                ten, lxMa, moTa
        );
    }

    public boolean capNhatKhuVuc(
            long ma, String ten, long lxMa,
            String moTa, String trangThai) throws SQLException {
        return update(
                "UPDATE khu_vuc SET kv_ten_khu_vuc=?, lx_ma=?, " +
                        "kv_mo_ta=?, kv_trang_thai=? WHERE kv_ma=?",
                ten, lxMa, moTa, trangThai, ma
        ) > 0;
    }

    public boolean capNhatTrangThai(long ma, String trangThai)
            throws SQLException {
        return update(
                "UPDATE khu_vuc SET kv_trang_thai=? WHERE kv_ma=?",
                trangThai, ma
        ) > 0;
    }

    public List<Map<String, Object>> thongKeSucChua()
            throws SQLException {
        return query(
                "SELECT kv.kv_ma, kv.kv_ten_khu_vuc, " +
                        "kv.kv_trang_thai, " +
                        "COUNT(c.cdx_ma) AS tong_cho, " +
                        "COUNT(c.cdx_ma) FILTER " +
                        "(WHERE c.cdx_trang_thai='TRONG') AS cho_trong, " +
                        "COUNT(c.cdx_ma) FILTER " +
                        "(WHERE c.cdx_trang_thai='DANG_CO_XE') AS cho_co_xe, " +
                        "COUNT(c.cdx_ma) FILTER " +
                        "(WHERE c.cdx_trang_thai='BAO_TRI') AS cho_bao_tri " +
                        "FROM khu_vuc kv LEFT JOIN cho_dau_xe c " +
                        "ON kv.kv_ma=c.kv_ma " +
                        "GROUP BY kv.kv_ma ORDER BY kv.kv_ma"
        );
    }
}