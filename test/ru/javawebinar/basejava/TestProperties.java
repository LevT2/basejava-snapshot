package ru.javawebinar.basejava;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TestProperties {
    @Test
    public void write() {
        try (OutputStream outputStream = new FileOutputStream("test.properties")){
            Properties prop = new Properties();

            prop.setProperty("contact.1", "Тел.: +7(921) 855-0482");
            prop.setProperty("contact.2", "Skype: <a href='skype:grigory.kislin'>grigory.kislin</a>");
            prop.setProperty("contact.3", "Почта: <a href='mailto:gkislin@yandex.ru'>gkislin@yandex.ru");

            prop.store(outputStream, null);
            System.out.println(prop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read(){
        try (InputStream inputStream = new FileInputStream("test/resources/testdata.experience.properties")) {
            Properties prop = new Properties();

            prop.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

            for (Object o : prop.keySet()) {
                System.out.println(prop.get(o));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
