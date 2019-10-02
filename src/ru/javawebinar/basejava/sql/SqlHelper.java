package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public interface ABlockOfCode {
        Resume execute(ConnectionFactory connectionFactory, PreparedStatement ps, String sql, String... params) throws SQLException;
    }

    public static Resume execute(ConnectionFactory connectionFactory, ABlockOfCode aBlockOfCode, String sql, String... params) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return aBlockOfCode.execute(connectionFactory, ps, sql, params);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

}
