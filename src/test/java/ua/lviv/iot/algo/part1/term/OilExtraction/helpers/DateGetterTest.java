package ua.lviv.iot.algo.part1.term.OilExtraction.helpers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateGetterTest {

    @Test
    public void testGetDate() {
        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String actualDate = DateGetter.getDate();

        Assertions.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetMonth() {
        String expectedMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String actualMonth = DateGetter.getMonth();

        Assertions.assertEquals(expectedMonth, actualMonth);
    }
}
