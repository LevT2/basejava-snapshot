package ru.javawebinar.basejava;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.System.getProperty;

public class MainRecursion {
    protected static final String ROOT = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException {
        Path path = Paths.get(getProperty("user.dir"), "src");
//        prettyPrint(path);
    }

// requires higher java incompatible with JAXB
//    private static void prettyPrint(Path path) throws IOException {
//
//        Files.walk(path).
//                filter(Files::isDirectory).
//                forEach(childpath -> {
//                    System.out.println(childpath);
//                    try ( DirectoryStream files= Files.newDirectoryStream(childpath, Files::isRegularFile)) {
//                        files.forEach(file -> {
//                            System.out.print(" ".repeat(file.getParent().toString().length()));
//                            System.out.println(file.getFileName().toString());
//                        });
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//    }
}
