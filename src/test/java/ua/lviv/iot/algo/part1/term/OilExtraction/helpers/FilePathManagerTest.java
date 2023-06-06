package ua.lviv.iot.algo.part1.term.OilExtraction.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FilePathManagerTest {

    @Test
    public void testGetFileName() {
        Rig rig = new Rig(1, -23.5, 12.7, new HashSet<>());
        String expectedFileName = "src/main/resources/entities/rig-" + DateGetter.getDate() + ".csv";
        String actualFileName = FilePathManager.getFileName(rig);

        Assertions.assertEquals(expectedFileName, actualFileName);
    }

    @Test
    public void testGetFilesCreatedInThisMonth() {
        List<String> files = new ArrayList<>();
        files.add("rig-2023-06-01.csv");
        files.add("rig-2023-06-02.csv");
        files.add("rig-2023-05-15.csv");

        List<String> expectedFiles = new ArrayList<>();
        expectedFiles.add("rig-2023-06-01.csv");
        expectedFiles.add("rig-2023-06-02.csv");

        List<String> actualFiles = FilePathManager.getFilesCreatedInThisMonth(files);

        Assertions.assertEquals(expectedFiles, actualFiles);
    }

    @Test
    public void testGetFilesCreatedNotInThisMonth() {

        List<String> files = new ArrayList<>();
        files.add("rig-2023-02-01.csv");
        files.add("rig-2023-01-02.csv");
        files.add("rig-2023-04-15.csv");

        List<String> expectedFiles = new ArrayList<>();

        Assertions.assertEquals(expectedFiles, FilePathManager.getFilesCreatedInThisMonth(files));
    }
}
