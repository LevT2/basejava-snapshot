package ru.javawebinar.basejava.storage.serializer;

import com.sun.org.apache.xpath.internal.operations.Or;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            // TODO implements sections

            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                dos.writeUTF(entry.getValue().getClass().getName());
                dos.writeUTF(entry.getKey().name());   //

                writeSection(dos, entry.getValue(), entry.getKey());
            }
        }
    }

    private void writeSection(DataOutputStream dos, Section section, SectionType type) throws IOException {
        String className = section.getClass().getName();
//        dos.writeUTF(className);

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
                for (String item : listSection.getList()) {
                    dos.writeUTF(item);
                }
                break;
            case EXPERIENCE:
            case EDUCATION:
                OrganizationSection organizationSection = (OrganizationSection) section;
                dos.writeInt(organizationSection.getList().size());
                for (Organization organization : organizationSection.getList()) {
                    dos.writeUTF(organization.getName());
                    dos.writeUTF(wrapNull(organization.getUrl()));

                    List<Organization.Position> positions = organization.getPositions();
                    dos.writeInt(positions.size());
                    for (Organization.Position position : positions) {
                        dos.writeUTF(position.getStart().toString());
                        dos.writeUTF(position.getStart().toString());
                        dos.writeUTF(position.getTitle());
                        dos.writeUTF(wrapNull(position.getDescription()));
                    }
                }

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

            // TODO implements sections
            int sectionSize = dis.readInt();
            for (int i = 0; i < sectionSize; i++) {
                String className = dis.readUTF();
                String enumValue = dis.readUTF();
                System.out.println("==>>" + className + "  " + enumValue);

                SectionType sectionType = SectionType.valueOf(enumValue);
                Class aClass;
                try {
                    aClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new IOException("Could not find class: " + className);
                }
                Section section = (Section) aClass.newInstance();

                section = readSection(dis, section, sectionType);
                resume.addSection(sectionType, section);
            }
            return resume;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IOException("Class loading failed", e);
        }
    }


    private Section readSection(DataInputStream dis, Section section, SectionType type) throws IOException {
        String className = section.getClass().getName();
//        System.out.printf("===> " + className);

        int size;
        switch (type) {
            case OBJECTIVE:
            case PERSONAL:
                TextSection textSection = (TextSection) section;
                textSection.setText(dis.readUTF());
                return textSection;

            case ACHIEVEMENT:
            case QUALIFICATIONS:
                ListSection listSection = (ListSection) section;
                size = dis.readInt();
                for (int i = 0; i < size ; i++) {
                    listSection.getList().add(dis.readUTF());
                }
                return listSection;

            case EDUCATION:
            case EXPERIENCE:
                OrganizationSection organizationSection = (OrganizationSection) section;
                size = dis.readInt();
                for (int i = 0; i < size ; i++) {
                    String name = dis.readUTF();
                    String url = unwrapNull(dis.readUTF());

                    Organization organization = new Organization(name, url);
                    int size2 = dis.readInt();
                    List<Organization.Position> positions = new ArrayList<>(size2);
                    for (int y = 0; y < size2 ; y++) {
                        YearMonth start = YearMonth.parse(dis.readUTF());
                        YearMonth end = YearMonth.parse(dis.readUTF());
                        String title = dis.readUTF();
                        String desc = unwrapNull(dis.readUTF());
                        Organization.Position position = new Organization.Position(
                                start,end,title,desc
                        );
                        organization.addRecord(position);
//                        positions.add(position);
                    }
//                    Organization organization = new Organization(name, url, positions);
                    organizationSection.getList().add(organization);
                }
                return organizationSection;

            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private String wrapNull(String data) {
        return data == null ? "NULLOBJECT" : data;
    }

    private String unwrapNull(String data) {
        return data.equals("NULLOBJECT") ? null : data;
    }

}

