package ru.javawebinar.basejava;


import ru.javawebinar.basejava.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ResumeFromProperties {

    private Resume resume;

    public Resume build() {
        resume = new Resume(UUID.randomUUID().toString(), "Григорий Кислин");

        String path = System.getProperty("user.dir") + "/";

        String rootProperties = path + "testdata.properties";
        try (InputStream inputStream = new FileInputStream(rootProperties)) {
            readParentProperties(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String experienceProperties = path + "testdata.experience.properties";
        try (InputStream inputStream = new FileInputStream(experienceProperties)) {
            readChildProperties(inputStream, SectionType.EXPERIENCE, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String educationProperties = path + "testdata.education.properties";
        try (InputStream inputStream = new FileInputStream(educationProperties)) {
            readChildProperties(inputStream, SectionType.EDUCATION, 7);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resume;
    }

    private void readChildProperties(InputStream inputStream, SectionType sectionType, int count) {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Organization> list;
        list = readList(prop, count);
        resume.addSection(sectionType, new OrganizationSection(list));
    }

    private List<Organization> readList(Properties prop, int count) {
        List<Organization> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/uuuu");

        for (int i = 1; i <= count; i++) {
            String prefix = String.format("%d", i) + ".";

            Integer syear = Integer.valueOf(prop.getProperty(prefix + "start").substring(3,7));
            Integer smonth = Integer.valueOf(prop.getProperty(prefix + "start").substring(0,2));

            Integer eyear = Integer.valueOf(prop.getProperty(prefix + "start").substring(3,7));
            Integer emonth = Integer.valueOf(prop.getProperty(prefix + "start").substring(0,2));

            Organization item = new Organization(
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
}
