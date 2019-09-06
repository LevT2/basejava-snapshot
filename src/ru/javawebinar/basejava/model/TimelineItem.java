package ru.javawebinar.basejava.model;

import java.time.YearMonth;
import java.util.Objects;

public class TimelineItem {

    private final String name;
    private final YearMonth start;
    private final YearMonth end;
    private final String html;

    public TimelineItem(String name, String url, YearMonth start, YearMonth end, String html) {
        Objects.requireNonNull(name, "title must not be null");
        this.name = name;
        this.start = start;
        this.end = end;
        this.html = html;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimelineItem that = (TimelineItem) o;
        return  name.equals(that.name) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(html, that.html);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, start, end);
    }

    @Override
    public String toString() {
        return "TimelineItem{" +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", html='" + html + '\'' +
                '}';
    }
}
