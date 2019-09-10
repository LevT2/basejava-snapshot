package ru.javawebinar.basejava;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestProperties {

    private Resume resume;

    @Before
    public void setUp() {
        resume = new ResumeFromProperties().build();;
    }

    @Test
    public void readPropertiesToResume() {
        StringBuilder builder = new StringBuilder();

//        builder.append(resume);
        builder.append(System.getProperty("line.separator"));
        builder.append(System.getProperty("line.separator"));

        buildContacts(builder);

        buildSection(SectionType.valueOf("OBJECTIVE"), builder);
        buildSection(SectionType.valueOf("PERSONAL"), builder);
        buildSection(SectionType.valueOf("ACHIEVEMENT"), builder);
        buildSection(SectionType.valueOf("QUALIFICATIONS"), builder);
        buildSection(SectionType.valueOf("EXPERIENCE"), builder);
        buildSection(SectionType.valueOf("EDUCATION"), builder);

        System.out.println(builder);
    }


    private void buildSection(SectionType sectionType, StringBuilder builder) {
        switch (sectionType) {
            case OBJECTIVE:
            case PERSONAL:
                builder.append(System.getProperty("line.separator"));

                builder.append(sectionType.toString());
                builder.append(System.getProperty("line.separator"));

                builder.append(resume.getSection(sectionType));
                builder.append(System.getProperty("line.separator"));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                buildListSection(sectionType, builder);
                break;
            case EXPERIENCE:
            case EDUCATION:
                buildTimelineSection(sectionType, builder);
        }
    }

    private void buildListSection(SectionType sectionType, StringBuilder builder) {
        builder.append(System.getProperty("line.separator"));
        builder.append(sectionType.toString());
        builder.append(resume.getSection(sectionType));
    }

    private void buildTimelineSection(SectionType sectionType, StringBuilder builder) {
        builder.append(System.getProperty("line.separator"));
        builder.append(sectionType.toString());
        builder.append(resume.getSection(sectionType));
    }


//    private void buildTimelineSection(SectionType sectionType, StringBuilder builder) {
//        builder.append(System.getProperty("line.separator"));
//        builder.append(sectionType.toString());
//        OrganizationSection section = (OrganizationSection) resume.getSection(sectionType);
//        for (int i = 0; i < section.getList().size(); i++) {
//            Organization organization = section.getList().get(i);
//            builder.append(organization.toString());
//        }
////        builder.append(section.getList());
//    }


    private void buildContacts(StringBuilder builder) {

        builder.append("CONTACTS");
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.PHONE));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.SKYPE));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.MAIL));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.LINKEDIN));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.GITHUB));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.STATCKOVERFLOW));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.HOME_PAGE));
        builder.append(System.getProperty("line.separator"));
    }
}
