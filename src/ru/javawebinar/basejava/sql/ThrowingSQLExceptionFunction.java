package ru.javawebinar.basejava.sql;
import java.sql.SQLException;
import java.util.function.Function;

@FunctionalInterface
public interface ThrowingSQLExceptionFunction<T,R> extends Function<T,R> {
    @Override
    default R apply(final T elem) {
        try {
            return applyThrows(elem);
        } catch (final SQLException e) {
            // Implement your own exception handling logic here..
            // For example:
            System.out.println("handling an exception...");
            // Or ...
            throw new RuntimeException(e);
        }
    }

    R applyThrows(T elem) throws SQLException;
}