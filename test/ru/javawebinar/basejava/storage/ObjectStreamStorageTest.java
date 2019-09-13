package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ObjectStreamStorageTest extends AbstractStorageTest {
    public ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }
}