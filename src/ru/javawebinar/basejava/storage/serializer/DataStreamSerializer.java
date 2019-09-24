package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.*;


public class DataStreamSerializer implements StreamSerializer {

    @FunctionalInterface
    interface ThrowingConsumer<T> {
        void accept(T t) throws IOException;
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws IOException;
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            writeCollection(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            Map<SectionType, Section> sections = r.getSections();
            writeCollection(dos, sections.entrySet(), entry -> {
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
                writeCollection(dos, listSection.getList(), dos::writeUTF);
                break;

            case EXPERIENCE:
            case EDUCATION:
                OrganizationSection organizationSection = (OrganizationSection) section;
                writeCollection(dos, organizationSection.getList(), organization -> {
                    dos.writeUTF(organization.getName());
                    dos.writeUTF(wrapNull(organization.getUrl()));

                    writeCollection(dos, organization.getPositions(), position -> {
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

            readSequence(dis, () -> {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
                return 1; // new Object(); // ArrayList(); // resume.getSections();  // resume.getCollections();   WHATEVER!
            });

            readSequence(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());

                Section section = readSection(dis, sectionType);
                resume.addSection(sectionType, section);
                return null; //new ArrayList(); // resume.getSections();  WHATEVER!!  Just to comply with the Java compiler
            });

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
                ListSection listSection = new ListSection(readCollection(dis, dis::readUTF));
                return listSection;

            case EDUCATION:
            case EXPERIENCE:
                OrganizationSection organizationSection = new OrganizationSection(readCollection(dis, () -> {
                    Organization organization = new Organization(
                            dis.readUTF(),
                            unwrapNull(dis.readUTF()),
                            readCollection(dis, () -> {
                                Organization.Position position = new Organization.Position(
                                        YearMonth.parse(dis.readUTF()),
                                        YearMonth.parse(dis.readUTF()),
                                        dis.readUTF(),
                                        unwrapNull(dis.readUTF()));
                                return position;
                            }));
                    return organization;
                }));
                return organizationSection;
            default:
                throw new IllegalStateException("Unexpected value: " + sectionType);
        }
    }


    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, ThrowingConsumer<? super T> action) throws IOException {
        Objects.requireNonNull(action);
        dos.writeInt(collection.size());
        for (T t : collection) {
            action.accept(t);
        }
    }

    private <T> List<T> readCollection(DataInputStream dis, ThrowingSupplier<? extends T> supplier) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    private <T> void readSequence(DataInputStream dis, ThrowingSupplier<? extends T> supplier) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            supplier.get();
        }
    }


    private String wrapNull(String data) {
        return data == null ? "" : data;
    }

    private String unwrapNull(String data) {
        return data.equals("") ? null : data;
    }
}

