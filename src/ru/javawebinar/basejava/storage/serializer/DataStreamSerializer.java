package ru.javawebinar.basejava.storage.serializer;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Supplier;


public class DataStreamSerializer implements StreamSerializer {

    @FunctionalInterface
    interface ThrowingConsumer<T> {
        void accept(T t) throws IOException;
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, ThrowingConsumer<? super T> action) throws IOException {
        Objects.requireNonNull(action);
        dos.writeInt(collection.size());
        for (T t : collection) {
            action.accept(t);
        }
    }

//    private String readString(DataInputStream dis, ThrowingSupplier<String> supplier) throws IOException {
//        int size = dis.readInt();
//        for (int i = 0; i < size; i++) {
//            return supplier.get();
//        }
//    }

    private <T> List<T> readCollection(DataInputStream dis, ThrowingSupplier<? extends T> supplier) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(supplier.get());
        }
        return list;
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
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

//            readCollection(dis, () -> {
//                        String enumValue = dis.readUTF();
//                        SectionType sectionType = SectionType.valueOf(enumValue);
//
//                        Section section = readSection(dis, sectionType);
//                        resume.addSection(sectionType, section);
//                    });


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
                ListSection listSection = new ListSection(readCollection(dis, (ThrowingSupplier) dis::readUTF));


//                ListSection listSection = new ListSection();
//                int listSize = dis.readInt();
//                for (int i = 0; i < listSize; i++) {
//                    listSection.getList().add(dis.readUTF());
//                }
//                return listSection;

            case EDUCATION:
            case EXPERIENCE:
                OrganizationSection section = new OrganizationSection(readCollection(dis, () -> {
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
                return section;
            default:
                throw new IllegalStateException("Unexpected value: " + sectionType);
        }
    }


    private String wrapNull(String data) {
        return data == null ? "" : data;
    }

    private String unwrapNull(String data) {
        return data.equals("") ? null : data;
    }
}

