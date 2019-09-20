package ru.javawebinar.basejava.util;

public class Util {
    public static String nullString(String param) {
        if (null == param ) {
            return "NULLSTRINGOBJECT";
        } else {
            return param;
        }
    }
}
