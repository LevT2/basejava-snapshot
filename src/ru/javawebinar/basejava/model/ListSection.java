package ru.javawebinar.basejava.model;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    private static final long serialVersionUID = 1L;

    private List<String> list;

    public ListSection() {
    }

    public ListSection(String... items) {
        this(Arrays.asList(items));
    }

    public ListSection(List<String> list) {
        Objects.requireNonNull(list, "you must provide a list");
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }


    @Override
    public void readData(DataInputStream in) throws IOException {
        int size = in.readInt();
        for (int i=0; i< size; i++){
            list.add(in.readUTF());
        }
    }

    @Override
    public void writeData(DataOutputStream out) throws IOException {
        out.writeInt(list.size());
        for (String item : list) {
            out.writeUTF(item);
        }
    }
}
