package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecutor<R> extends ThrowingSQLExceptionFunction<PreparedStatement, R> {
    R applyThrows(PreparedStatement ps) throws SQLException;
}
