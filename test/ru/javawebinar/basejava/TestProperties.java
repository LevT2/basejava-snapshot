package ru.javawebinar.basejava;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestProperties {

    private Resume resume;

    @Before
    public void setUp() {
        resume = new Resume(UUID.randomUUID().toString(), "Григорий Кислин");
    }


    @Test
    public void readPropertiesToResume() {
        try (InputStream inputStream = new FileInputStream("test/resources/testdata.properties")) {
            readParentProperties(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String name = "test/resources/testdata.experience.properties";
        try (InputStream inputStream = new FileInputStream(name)) {
            readChildProperties(inputStream, SectionType.EXPERIENCE, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String educationProperties = "test/resources/testdata.education.properties";
        try (InputStream inputStream = new FileInputStream(educationProperties)) {
            readChildProperties(inputStream, SectionType.EDUCATION, 7);
        } catch (IOException e) {
            e.printStackTrace();
        }


        StringBuilder builder = new StringBuilder();

        builder.append(resume);
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

    private void readChildProperties(InputStream inputStream, SectionType sectionType, int count) {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<TimelineItem> list;
        list = readList(prop, count);
        resume.addSection(sectionType, new TimelineSection(list));
    }

    private List<TimelineItem> readList(Properties prop, int count) {
        List<TimelineItem> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/uuuu");

        for (int i = 1; i <= count; i++) {
            String prefix = String.format("%d", i) + ".";

            Integer syear = Integer.valueOf(prop.getProperty(prefix + "start").substring(3,7));
            Integer smonth = Integer.valueOf(prop.getProperty(prefix + "start").substring(0,2));

            Integer eyear = Integer.valueOf(prop.getProperty(prefix + "start").substring(3,7));
            Integer emonth = Integer.valueOf(prop.getProperty(prefix + "start").substring(0,2));

            TimelineItem item = new TimelineItem(
                    prop.getProperty(prefix + "name"),
                    prop.getProperty(prefix + "url"),
//                    YearMonth.parse(prop.getProperty(prefix + "start"), formatter),
//                    YearMonth.parse(prop.getProperty(prefix + "end"), formatter),
                    YearMonth.of(syear,smonth),
                    YearMonth.of(eyear,emonth),
                    prop.getProperty(prefix + "html")
            );
            list.add(item);
        }
        return list;
    }

    private void readParentProperties(InputStream inputStream) throws IOException {
        Properties prop = new Properties();

        prop.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        resume.addContact(ContactType.PHONE, prop.getProperty("contact.phone"));
        resume.addContact(ContactType.SKYPE, prop.getProperty("contact.skype"));
        resume.addContact(ContactType.EMAIL, prop.getProperty("contact.email"));
        resume.addContact(ContactType.LINKEDIN, prop.getProperty("contact.linkedin"));
        resume.addContact(ContactType.GITHUB, prop.getProperty("contact.github"));
        resume.addContact(ContactType.SO, prop.getProperty("contact.so"));
        resume.addContact(ContactType.HOMEPAGE, prop.getProperty("contact.homepage"));

        resume.addSection(SectionType.OBJECTIVE, new TextSection(prop.getProperty("objective")));
        resume.addSection(SectionType.PERSONAL, new TextSection(prop.getProperty("personal")));

        List<String> listA = Arrays.asList(
                prop.getProperty("achievement.0"),
                prop.getProperty("achievement.1"),
                prop.getProperty("achievement.2"),
                prop.getProperty("achievement.3"),
                prop.getProperty("achievement.4"),
                prop.getProperty("achievement.5")
        );
        resume.addSection(SectionType.ACHIEVEMENT, new ListSection(listA));

        List<String> listQ = Arrays.asList(
                prop.getProperty("qualification.0"),
                prop.getProperty("qualification.1"),
                prop.getProperty("qualification.2"),
                prop.getProperty("qualification.3"),
                prop.getProperty("qualification.4"),
                prop.getProperty("qualification.5"),
                prop.getProperty("qualification.6"),
                prop.getProperty("qualification.7"),
                prop.getProperty("qualification.8"),
                prop.getProperty("qualification.9"),
                prop.getProperty("qualification.10"),
                prop.getProperty("qualification.11"),
                prop.getProperty("qualification.12"),
                prop.getProperty("qualification.13"),
                prop.getProperty("qualification.14")
        );
        resume.addSection(SectionType.QUALIFICATIONS, new ListSection(listQ));
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

    private void buildContacts(StringBuilder builder) {

        builder.append("CONTACTS");
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.PHONE));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.SKYPE));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.EMAIL));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.LINKEDIN));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.GITHUB));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.SO));
        builder.append(System.getProperty("line.separator"));

        builder.append(resume.getContact(ContactType.HOMEPAGE));
        builder.append(System.getProperty("line.separator"));
    }
}
