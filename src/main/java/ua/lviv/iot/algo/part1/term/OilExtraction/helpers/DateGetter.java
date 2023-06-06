package ua.lviv.iot.algo.part1.term.OilExtraction.helpers;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
public class DateGetter {

    public static String getDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(dateFormatter);
    }

    public static String getMonth() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return currentDate.format(formatter);
    }
}
