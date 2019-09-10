package ru.javawebinar.basejava.model;

import java.time.YearMonth;
import java.util.Objects;

public class Record {
    private YearMonth start;
    private YearMonth end;
    private String title;
    private String description;

    public Record(YearMonth start, YearMonth end, String title, String description) {
        Objects.requireNonNull(start, "you must provide the start date");
        Objects.requireNonNull(end, "you must provide the end date");
        Objects.requireNonNull(title, "you must provide the title");
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
    }

    public YearMonth getStart() {
        return start;
    }

    public YearMonth getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return start.equals(record.start) &&
                end.equals(record.end) &&
                title.equals(record.title) &&
                Objects.equals(description, record.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, title, description);
    }

    @Override
    public String toString(){
        return title;
    }
}
