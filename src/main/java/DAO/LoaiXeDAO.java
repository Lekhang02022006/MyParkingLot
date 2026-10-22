package DAO;

import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.*;

public class LoaiXeDAO extends BaseDAO {

    public Map<String, Object> timTheoMa(long ma)
            throws SQLException {
        return firstOrNull(query(
                "SELECT * FROM loai_xe WHERE lx_ma=?", ma
        ));
    }

    public List<Map<String, Object>> layTatCa()
            throws SQLException {
        return query(
                "SELECT * FROM loai_xe ORDER BY lx_ma"
        );
    }

    public List<Map<String, Object>> layLoaiXeHoatDong()
            throws SQLException {
        return query(
                "SELECT * FROM loai_xe " +
                        "WHERE lx_trang_thai='HOAT_DONG' ORDER BY lx_ma"
        );
    }

    public long themLoaiXe(
            String tenLoai, BigDecimal giaTheoGio, String moTa)
            throws SQLException {
        return insertAndGetId(
                "INSERT INTO loai_xe " +
                        "(lx_ten_loai, lx_gia_theo_gio, lx_mo_ta) " +
                        "VALUES (?, ?, ?)",
                tenLoai, giaTheoGio, moTa
        );
    }

    public boolean capNhatLoaiXe(
            long ma, String tenLoai, BigDecimal giaTheoGio,
            String moTa, String trangThai) throws SQLException {
        return update(
                "UPDATE loai_xe SET lx_ten_loai=?, " +
                        "lx_gia_theo_gio=?, lx_mo_ta=?, lx_trang_thai=? " +
                        "WHERE lx_ma=?",
                tenLoai, giaTheoGio, moTa, trangThai, ma
        ) > 0;
    }

    public boolean capNhatGia(long ma, BigDecimal giaMoi)
            throws SQLException {
        return update(
                "UPDATE loai_xe SET lx_gia_theo_gio=? WHERE lx_ma=?",
                giaMoi, ma
        ) > 0;
    }

    public boolean capNhatTrangThai(long ma, String trangThai)
            throws SQLException {
        return update(
                "UPDATE loai_xe SET lx_trang_thai=? WHERE lx_ma=?",
                trangThai, ma
        ) > 0;
    }
}