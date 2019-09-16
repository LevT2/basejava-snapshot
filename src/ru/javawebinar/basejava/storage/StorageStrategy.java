package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;

public interface StorageStrategy {
    abstract void doWrite(Resume resume, Object output) throws IOException;

    abstract Resume doRead(Object input) throws IOException;
}
