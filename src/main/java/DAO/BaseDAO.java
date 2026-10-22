package DAO;

import config.DBConnection;

import java.sql.*;
import java.util.*;

abstract class BaseDAO {

    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    protected List<Map<String, Object>> query(
            String sql, Object... params) throws SQLException {

        List<Map<String, Object>> result = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int count = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();

                    for (int i = 1; i <= count; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }

                    result.add(row);
                }
            }
        }

        return result;
    }

    protected int update(String sql, Object... params)
            throws SQLException {

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeUpdate();
        }
    }

    protected long insertAndGetId(String sql, Object... params)
            throws SQLException {

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Không lấy được ID vừa tạo.");
    }

    protected Map<String, Object> firstOrNull(
            List<Map<String, Object>> rows) {
        return rows.isEmpty() ? null : rows.get(0);
    }
}