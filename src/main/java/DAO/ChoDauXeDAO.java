package DAO;

import java.sql.SQLException;
import java.util.*;

public class ChoDauXeDAO extends BaseDAO {

    public Map<String, Object> timTheoMa(long ma)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM cho_dau_xe WHERE cdx_ma=?", ma
        ));
    }

    public List<Map<String, Object>> layTheoKhuVuc(long kvMa)
            throws SQLException {
        return query(
                "SELECT * FROM cho_dau_xe " +
                        "WHERE kv_ma=? ORDER BY cdx_ma_cho",
                kvMa
        );
    }

    public List<Map<String, Object>> layChoTrong(long kvMa)
            throws SQLException {
        return query(
                "SELECT * FROM cho_dau_xe " +
                        "WHERE kv_ma=? AND cdx_trang_thai='TRONG' " +
                        "ORDER BY cdx_ma_cho",
                kvMa
        );
    }

    public long themCho(long kvMa, String maCho)
            throws SQLException {
        return insertAndGetId(
                "INSERT INTO cho_dau_xe (kv_ma, cdx_ma_cho) " +
                        "VALUES (?, ?)",
                kvMa, maCho
        );
    }

    public boolean capNhatTrangThai(long ma, String trangThai)
            throws SQLException {
        return update(
                "UPDATE cho_dau_xe SET cdx_trang_thai=? " +
                        "WHERE cdx_ma=?",
                trangThai, ma
        ) > 0;
    }

    public boolean datChoBaoTri(long ma) throws SQLException {
        return capNhatTrangThai(ma, "BAO_TRI");
    }

    public boolean moBaoTri(long ma) throws SQLException {
        return capNhatTrangThai(ma, "TRONG");
    }

    public boolean xoaCho(long ma) throws SQLException {
        return update(
                "DELETE FROM cho_dau_xe WHERE cdx_ma=?", ma
        ) > 0;
    }
}