package ru.javawebinar.basejava.storage.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Storable
{
    void readData(DataInputStream in) throws IOException;
    void writeData(DataOutputStream out) throws IOException;
}