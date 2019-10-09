package ru.javawebinar.basejava.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionExecutor<R> extends ThrowingSQLExceptionFunction<Connection, R> {
    R applyThrows(Connection conn) throws SQLException;
}
