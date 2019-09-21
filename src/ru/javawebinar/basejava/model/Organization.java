package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.YearMonthAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.basejava.util.DateUtil.NOW;
import static ru.javawebinar.basejava.util.DateUtil.of;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String url;

    private List<Position> positions = new ArrayList<>();

    public Organization() {
    }

    public Organization(String name, String url, Position... positions) {
        this(name, url);
        this.positions = Arrays.asList(positions);
    }

    public Organization(String name, String url, List<Position> positions) {
        this(name, url);
        this.positions = positions;
    }

    public Organization(String name, String url){
        this.name = name;
        this.url = url;
    }

    public void addRecord(Position position) {
        positions.add(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return  name.equals(that.name) &&
                Objects.equals(url, that.url) &&
                Objects.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, positions);
    }

    @Override
    public String toString() {
        return  name + " : " + positions.toString();
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth start;
        @XmlJavaTypeAdapter(YearMonthAdapter.class)
        private YearMonth end;
        private String title;
        private String description;

        public Position() {
        }

        public Position(int startYear, Month startMonth, String title, String description) {
            this(of(startYear, startMonth), NOW, title, description);
        }

        public Position(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(of(startYear, startMonth), of(endYear, endMonth), title, description);
        }

        public Position(YearMonth start, YearMonth end, String title, String description) {
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
        public String toString(){
            return title;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return start.equals(position.start) &&
                    end.equals(position.end) &&
                    title.equals(position.title) &&
                    Objects.equals(description, position.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end, title, description);
        }
    }
}
