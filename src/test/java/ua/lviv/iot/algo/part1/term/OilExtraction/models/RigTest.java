package ua.lviv.iot.algo.part1.term.OilExtraction.models;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class RigTest {

    @Test
    public void testGetHeaders() {
        Rig rig = new Rig();
        String expectedHeaders = "id;longitude;latitude";
        String actualHeaders = rig.getHeaders();

        Assertions.assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testToCSV() {
        Rig rig = new Rig(1, -23.5, 12.7, new HashSet<>());
        String expectedCSV = "1;-23.5;12.7";
        String actualCSV = rig.toCSV();

        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    public void testSettersAndGetters() {
        Rig rig = new Rig();
        rig.setId(1);
        rig.setLongitude(-23.5);
        rig.setLatitude(12.7);

        Assertions.assertEquals(1, rig.getId());
        Assertions.assertEquals(-23.5, rig.getLongitude());
        Assertions.assertEquals(12.7, rig.getLatitude());
    }

    @Test
    public void testTankers() {
        Set<Tanker> tankers = new HashSet<>();
        Tanker tanker1 = new Tanker();
        Tanker tanker2 = new Tanker();

        tankers.add(tanker1);
        tankers.add(tanker2);

        Rig rig = new Rig();
        rig.setTankers(tankers);

        Assertions.assertEquals(tankers, rig.getTankers());
    }
}
