package ru.javawebinar.basejava.storage;

import org.postgresql.util.PSQLException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlExecutor;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        LOG.info("Clearing");
        sqlHelper.execute("DELETE FROM resume");
    }


    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        SqlExecutor<Object> updateSqlExecutor = ps -> {
            ps.setString(1, resume.getFullName());

            ps.setString(2, resume.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            return null;
        };
        sqlHelper.execute("UPDATE resume SET full_name = ? WHERE uuid = ?", updateSqlExecutor);
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        SqlExecutor<Void> saveSqlExecutor = ps -> {
            ps.setString(1, resume.getUuid());
            ps.setString(2, resume.getFullName());
            ps.execute();
            return null;
        };
        sqlHelper.execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)", saveSqlExecutor);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        SqlExecutor<Resume> getSqlExecutor = ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        };
        return sqlHelper.execute("SELECT * FROM resume r WHERE r.uuid =?", getSqlExecutor);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        SqlExecutor<Object> deleteSqlExecutor = ps -> {      ////
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        };
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", deleteSqlExecutor);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        SqlExecutor<List<Resume>> getListSqlExecutor = ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return resumes;
        };
        return sqlHelper.execute("SELECT * FROM resume r ORDER BY full_name,uuid", getListSqlExecutor);
    }

    @Override
    public int size() {
        LOG.info("Getting size");
        SqlExecutor<Integer> integerSqlExecutor = st -> {
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        };
        return sqlHelper.execute("SELECT COUNT(*) FROM resume", integerSqlExecutor);
    }
}
