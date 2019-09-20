package ru.javawebinar.basejava.model;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.basejava.util.Util.nullString;

public class OrganizationSection extends Section {
    private static final long serialVersionUID = 1L;

    private List<Organization> list;

    public OrganizationSection() {}

    public OrganizationSection(Organization... organizations) {
        this(Arrays.asList(organizations));
    }

    public OrganizationSection(List<Organization> list) {
        Objects.requireNonNull(list, "you must provide the list");
        this.list = list;
    }

    public List<Organization> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return list.toString();
    }


    @Override
    public void readData(DataInputStream in) throws IOException {
        int size = in.readInt();
        for (int i=0; i< size; i++){
            String name = in.readUTF();
            String url = in.readUTF();
            Organization organization = new Organization(name, url);
            organization.readData(in);
            list.add(organization);
        }
    }

    @Override
    public void writeData(DataOutputStream out) throws IOException {
        out.writeInt(list.size());
        for (Organization organization : list) {
            out.writeUTF(nullString(organization.getName()));
            out.writeUTF(nullString(organization.getUrl()));
            organization.writeData(out);
        }
    }
}
