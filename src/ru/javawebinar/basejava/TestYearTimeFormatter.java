package ru.javawebinar.basejava;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class TestYearTimeFormatter
{

    public static void main(String... args) {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("M/uuuu");
            YearMonth y = YearMonth.parse("10/1990", f);
            System.out.println(y);
        }
}

