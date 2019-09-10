package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Organization {

    private final String name;
    private final String url;

    private List<Record> records = new ArrayList<>();

    public Organization(String name, String url){
        this.name = name;
        this.url = url;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return  name.equals(that.name) &&
                Objects.equals(url, that.url) &&
                Objects.equals(records, that.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, records);
    }

    @Override
    public String toString() {
        return  "\n" + name + " : " + records.toString();
    }
}
