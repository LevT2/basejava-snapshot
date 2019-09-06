package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class TimelineSection extends Section {
    private final List<TimelineItem> list;

    public TimelineSection(List<TimelineItem> list) {
        Objects.requireNonNull(list, "you must provide the list");
        this.list = list;
    }

    public List<TimelineItem> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimelineSection that = (TimelineSection) o;
        return list.equals(that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
