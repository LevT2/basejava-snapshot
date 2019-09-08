package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Тел.: "),
    SKYPE("Skype: "),
    EMAIL("Почта: "),
    LINKEDIN(null),
    GITHUB(null),
    SO(null),
    HOMEPAGE(null);

    private String label;
    private String text;

    ContactType(String label) {
        this.label = label;
    }

    public String withLink(String link) {
        return (label + link);
    }
}
