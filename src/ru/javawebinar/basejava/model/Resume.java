package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;

    // Unique identifier
    private final String uuid;

    private final String fullName;

    private Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);

    private Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public void  addContact(ContactType contactType, String value) {
        contacts.put(contactType, value);
    }

    public void  addSection(SectionType sectionType, AbstractSection section) {
        sections.put(sectionType, section);
    }

    public String getContact(ContactType contactType){
        return contacts.get(contactType);
    }


    public AbstractSection getSection(SectionType sectionType) {
        return sections.get(sectionType);
    }

    public String getUuid() {
        return uuid;
    }



    @Override
    public int compareTo(Resume o) {
        int cmp = fullName.compareTo(o.fullName);
        return cmp != 0 ? cmp : uuid.compareTo(o.uuid);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) &&
                fullName.equals(resume.fullName) &&
                sections.equals(resume.sections) &&
                contacts.equals(resume.contacts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, sections, contacts);
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sections=" + sections +
                ", contacts=" + contacts +
                '}';
    }
}