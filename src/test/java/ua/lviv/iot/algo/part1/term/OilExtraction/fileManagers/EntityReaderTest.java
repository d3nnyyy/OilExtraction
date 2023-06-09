package ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers;

import org.junit.jupiter.api.*;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Tanker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class EntityReaderTest {

    private static final String PATH = "src/main/resources/testEntities/";

    @BeforeEach
    public void deleteFileIfExists() throws IOException {
        for (File file : Objects.requireNonNull(new File(PATH).listFiles())) {
            Files.deleteIfExists(Path.of(file.getPath()));
        }
    }

    @Test
    public void readRig() {
        Rig expectedRig = new Rig(1, -10, 10, new HashSet<>());
        EntityWriter.writeToCSV(expectedRig, PATH + "rig-2023-06-05.csv");
        Map<Class<? extends Entity>, List<Entity>> entityMap = EntityReader.readEntities(PATH);
        List<Entity> rigList = entityMap.get(Rig.class);
        Rig actualRig = (Rig) rigList.get(0);
        Assertions.assertEquals(expectedRig.getId(), actualRig.getId());
        Assertions.assertEquals(expectedRig.getLongitude(), actualRig.getLongitude());
        Assertions.assertEquals(expectedRig.getLatitude(), actualRig.getLatitude());
    }

    @Test
    public void readTanker() {
        Tanker expectedTanker = new Tanker(1, 500000, 500000, new ArrayList<>(), new Rig(1, -20.1, 0.6, new HashSet<>()), 1);
        EntityWriter.writeToCSV(expectedTanker, PATH + "tanker-2023-06-05.csv");
        Map<Class<? extends Entity>, List<Entity>> entityMap = EntityReader.readEntities(PATH);
        List<Entity> tankerList = entityMap.get(Tanker.class);
        Tanker actualTanker = (Tanker) tankerList.get(0);
        actualTanker.setRig(new Rig(1, -20.1, 0.6, new HashSet<>()));
        Assertions.assertEquals(expectedTanker.getId(), actualTanker.getId());
        Assertions.assertEquals(expectedTanker.getCapacityInBarrels(), actualTanker.getCapacityInBarrels());
        Assertions.assertEquals(expectedTanker.getCurrentLoadInBarrels(), actualTanker.getCurrentLoadInBarrels());
        Assertions.assertEquals(expectedTanker.getRig().toString().trim(), actualTanker.getRig().toString().trim());
        Assertions.assertEquals(expectedTanker.getRigId(), actualTanker.getRigId());
    }

    @Test
    public void updateRig() {
        Rig rigToUpdate = new Rig(1, -10, 10, new HashSet<>());
        Rig rigWithNewValues = new Rig(1, -20.1, 0.6, new HashSet<>());
        EntityWriter.writeToCSV(rigToUpdate, PATH + "rig-2023-06-05.csv");
        EntityReader.updateEntityInCsv(rigWithNewValues, PATH);
        Map<Class<? extends Entity>, List<Entity>> entityMap = EntityReader.readEntities(PATH);
        List<Entity> rigList = entityMap.get(Rig.class);
        Rig actualRig = (Rig) rigList.get(0);
        Assertions.assertEquals(rigWithNewValues.getId(), actualRig.getId());
        Assertions.assertEquals(rigWithNewValues.getLongitude(), actualRig.getLongitude());
        Assertions.assertEquals(rigWithNewValues.getLatitude(), actualRig.getLatitude());
    }

    @Test
    public void updateTanker() {
        Rig someRig = new Rig(2, -11, 11, new HashSet<>());
        Tanker tankerToUpdate = new Tanker(1, 500000, 500000, new ArrayList<>(), new Rig(1, -20.1, 0.6, new HashSet<>()), 1);
        Tanker tankerWithNewValues = new Tanker(1, 1000000, 700000, new ArrayList<>(), someRig, someRig.getId());
        EntityWriter.writeToCSV(tankerToUpdate, PATH + "tanker-2023-06-05.csv");
        EntityReader.updateEntityInCsv(tankerWithNewValues, PATH);
        Map<Class<? extends Entity>, List<Entity>> entityMap = EntityReader.readEntities(PATH);
        List<Entity> tankerList = entityMap.get(Tanker.class);
        Tanker actualTanker = (Tanker) tankerList.get(0);
        actualTanker.setRig(someRig);
        Assertions.assertEquals(tankerWithNewValues.getId(), actualTanker.getId());
        Assertions.assertEquals(tankerWithNewValues.getCurrentLoadInBarrels(), actualTanker.getCurrentLoadInBarrels());
        Assertions.assertEquals(tankerWithNewValues.getCapacityInBarrels(), actualTanker.getCapacityInBarrels());
        Assertions.assertEquals(tankerWithNewValues.getRig().toString().trim(), actualTanker.getRig().toString().trim());
        Assertions.assertEquals(tankerWithNewValues.getRigId(), actualTanker.getRigId());
    }

    @Test
    public void deleteRig() {
        Rig rigToDelete = new Rig(1, -10, 10, new HashSet<>());
        EntityWriter.writeToCSV(rigToDelete, PATH + "rig-2023-06-05.csv");
        Map<Class<? extends Entity>, List<Entity>> entityMap = EntityReader.readEntities(PATH);
        List<Entity> rigList = entityMap.get(Rig.class);
        Assertions.assertEquals(1, rigList.size());
        EntityReader.deleteEntityFromCSV(rigToDelete, PATH);
        entityMap = EntityReader.readEntities(PATH);
        rigList = entityMap.get(Rig.class);
        Assertions.assertEquals(0, rigList.size());
    }

    @Test
    public void deleteTanker() {
        Tanker tankerToDelete = new Tanker(1, 500000, 500000, new ArrayList<>(), new Rig(1, -20.1, 0.6, new HashSet<>()), 1);
        EntityWriter.writeToCSV(tankerToDelete, PATH + "tanker-2023-06-05.csv");
        Map<Class<? extends Entity>, List<Entity>> entityMap = EntityReader.readEntities(PATH);
        List<Entity> tankerList = entityMap.get(Tanker.class);
        Assertions.assertEquals(1, tankerList.size());
        EntityReader.deleteEntityFromCSV(tankerToDelete, PATH);
        entityMap = EntityReader.readEntities(PATH);
        tankerList = entityMap.get(Tanker.class);
        Assertions.assertEquals(0, tankerList.size());
    }
}


