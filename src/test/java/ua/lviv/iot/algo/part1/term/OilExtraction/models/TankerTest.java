package ua.lviv.iot.algo.part1.term.OilExtraction.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TankerTest {

    @Test
    public void testGetHeaders() {
        Tanker tanker = new Tanker();
        String expectedHeaders = "id;capacityInBarrels;currentLoadInBarrels;datesOfDeparture;rigId";
        String actualHeaders = tanker.getHeaders();

        Assertions.assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testToCSV() {
        List<String> datesOfDeparture = new ArrayList<>();
        datesOfDeparture.add("2023-06-01");
        datesOfDeparture.add("2023-06-02");

        Tanker tanker = new Tanker(1, 500, 250, datesOfDeparture, null, 0);
        tanker.setRigId(1);

        String expectedCSV = "1;500;250;[2023-06-01, 2023-06-02];1";
        String actualCSV = tanker.toCSV();

        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    public void testGetRigId() {
        Rig rig = new Rig(1, -23.5, 12.7, new HashSet<>());
        Tanker tanker = new Tanker();
        tanker.setRig(rig);

        Assertions.assertEquals(1, tanker.getRigId());
    }

    @Test
    public void testSettersAndGetters() {
        List<String> datesOfDeparture = new ArrayList<>();
        datesOfDeparture.add("2023-06-01");
        datesOfDeparture.add("2023-06-02");

        Tanker tanker = new Tanker();
        tanker.setId(1);
        tanker.setCapacityInBarrels(500);
        tanker.setCurrentLoadInBarrels(250);
        tanker.setDatesOfDeparture(datesOfDeparture);
        tanker.setRigId(1);

        Assertions.assertEquals(1, tanker.getId());
        Assertions.assertEquals(500, tanker.getCapacityInBarrels());
        Assertions.assertEquals(250, tanker.getCurrentLoadInBarrels());
        Assertions.assertEquals(datesOfDeparture, tanker.getDatesOfDeparture());
        Assertions.assertEquals(1, tanker.getRigId());
    }

    @Test
    public void testRigAssociation() {
        Rig rig = new Rig(1, -23.5, 12.7, new HashSet<>());
        Tanker tanker = new Tanker();
        tanker.setRig(rig);

        Assertions.assertEquals(rig, tanker.getRig());
    }
}
