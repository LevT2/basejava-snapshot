package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileStorage extends AbstractStorage<File>  {
    private File directory;

    protected FileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        Arrays.stream(doListFiles())
                .forEach(this::doDelete);
    }

    @Override
    public int size() {
        return doListFiles().length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected void doWrite(Resume r, File file) throws IOException {};    // was abstract, not implemented

    protected Resume doRead(File file) throws IOException {             //added but not implemented
        return null;
    };

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("Error reading file", file.getName(), e);
        }

    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Error deleting file", file.getName());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        return Arrays.stream(doListFiles()).
                map(this::doGet).
                collect(Collectors.toCollection(ArrayList::new));
    }

    private File[] doListFiles(){
        File[] files = directory.listFiles();
        if(files == null) {
            throw new StorageException("Error listing directory", directory.getName());
        }
        return files;
    }
}
