package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume resume = new ResumeFromProperties().build();
        verifyResume(resume);
    }

    public static void verifyResume(Resume resume) {
        System.out.println(resume.toString());
    }

}
