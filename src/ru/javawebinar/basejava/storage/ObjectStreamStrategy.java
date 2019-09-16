package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamStrategy implements StorageStrategy {
    @Override
    public void doWrite(Resume resume, Object output) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream((OutputStream) output)) {
            objectOutputStream.writeObject(resume);
        }
    }

    @Override
    public Resume doRead(Object input) throws IOException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream((InputStream) input)) {
            return (Resume) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error reading resume", null, e);
        }
    }
}
