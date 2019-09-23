package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;


@FunctionalInterface
interface ThrowingConsumer<T> {
    void accept(T t) throws IOException;
}

public class DataStreamSerializer implements StreamSerializer {

    private <T> void forEachCustom(Collection<T> collection, ThrowingConsumer<? super T> action) throws IOException {
        Objects.requireNonNull(action);
        for (T t : collection) {
            action.accept(t);
        }
    }

    private <T> void writeCollection(DataOutputStream dos, Supplier<Collection<T>> method, ThrowingConsumer<? super T> action) throws IOException {
        Objects.requireNonNull(action);

        Collection<T> collection = method.get();
        dos.writeInt(collection.size());
        for (T t : collection) {
            action.accept(t);
        }
    }


    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            // не выходит
//            writeCollection(dos, r::getContacts, t -> {
//
//            })


            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            forEachCustom(contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            forEachCustom(sections.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                writeSection(dos, entry.getValue(), entry.getKey());
            });

        }
    }

    private void writeSection(DataOutputStream dos, Section section, SectionType type) throws IOException {
        switch (type) {
            case OBJECTIVE:
            case PERSONAL:
                TextSection textSection = (TextSection) section;
                dos.writeUTF(textSection.getText());
                break;

            case ACHIEVEMENT:
            case QUALIFICATIONS:
                ListSection listSection = (ListSection) section;
                dos.writeInt(listSection.getList().size());
                forEachCustom(listSection.getList(), dos::writeUTF);
                break;

            case EXPERIENCE:
            case EDUCATION:
                OrganizationSection organizationSection = (OrganizationSection) section;
                dos.writeInt(organizationSection.getList().size());

                forEachCustom(organizationSection.getList(), organization -> {
                    dos.writeUTF(organization.getName());
                    dos.writeUTF(wrapNull(organization.getUrl()));

                    List<Organization.Position> positions = organization.getPositions();
                    dos.writeInt(positions.size());

                    forEachCustom(positions, position -> {
                        dos.writeUTF(position.getStart().toString());
                        dos.writeUTF(position.getEnd().toString());
                        dos.writeUTF(position.getTitle());
                        dos.writeUTF(wrapNull(position.getDescription()));
                    });
                });
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }


    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sectionSize = dis.readInt();
            for (int i = 0; i < sectionSize; i++) {
                String enumValue = dis.readUTF();
                SectionType sectionType = SectionType.valueOf(enumValue);

                Section section = readSection(dis, sectionType);
                resume.addSection(sectionType, section);
            }
            return resume;
        }
    }


    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {

        switch (sectionType) {
            case OBJECTIVE:
            case PERSONAL:
                TextSection textSection = new TextSection();
                textSection.setText(dis.readUTF());
                return textSection;

            case ACHIEVEMENT:
            case QUALIFICATIONS:
                ListSection listSection = new ListSection();
                int listSize = dis.readInt();
                for (int i = 0; i < listSize; i++) {
                    listSection.getList().add(dis.readUTF());
                }
                return listSection;

            case EDUCATION:
            case EXPERIENCE:
                OrganizationSection organizationSection = new OrganizationSection();
                int list2Size = dis.readInt();
                for (int i = 0; i < list2Size; i++) {
                    String name = dis.readUTF();
                    String url = unwrapNull(dis.readUTF());

                    Organization organization = new Organization(name, url);
                    int size2 = dis.readInt();
                    List<Organization.Position> positions = new ArrayList<>(size2); //
                    for (int y = 0; y < size2; y++) {
                        YearMonth start = YearMonth.parse(dis.readUTF());
                        YearMonth end = YearMonth.parse(dis.readUTF());
                        String title = dis.readUTF();
                        String desc = unwrapNull(dis.readUTF());
                        Organization.Position position = new Organization.Position(start, end, title, desc);
                        organization.addRecord(position);
                    }
                    organizationSection.getList().add(organization);
                }
                return organizationSection;

            default:
                throw new IllegalStateException("Unexpected value: " + sectionType);
        }
    }

    private String wrapNull(String data) {
        return data == null ? "NULLOBJECT" : data;
    }

    private String unwrapNull(String data) {
        return data.equals("NULLOBJECT") ? null : data;
    }
}

