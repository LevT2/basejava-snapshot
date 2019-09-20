package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
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
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().toString());
                entry.getValue().writeData(dos);
            }

            // Хочу переключиться на этот вариант - как сделать чтобы компилировалось?
//            r.getSections().forEach((sectionType, section) -> {
//                dos.writeUTF(sectionType.toString());
//                section.writeData(dos);
//            });
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
            while(true) {
                try {
                    readSection(resume, dis);
                } catch (EOFException e){
                    break;
                }
            }

            return resume;
        }
    }

    private void readSection(Resume resume, DataInputStream dis) throws IOException {
        SectionType sectionType = SectionType.valueOf(dis.readUTF());
        Section section;
        switch (sectionType) {
            case OBJECTIVE:
            case PERSONAL:
                section = new TextSection();
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                section = new ListSection();
                break;
            case EXPERIENCE:
            case EDUCATION:
                section = new OrganizationSection();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sectionType);
        }
        section.readData(dis);
        resume.addSection(sectionType, section);
    }
}