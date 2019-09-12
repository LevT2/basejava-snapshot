package ru.javawebinar.basejava.storage;

import java.io.File;

public class FileStorageTest extends AbstractStorageTest {
    private static String path = System.getProperty("user.dir") + "/filedb";

    public FileStorageTest() {
        super(new FileStorage(new File(path)));
    }
}
