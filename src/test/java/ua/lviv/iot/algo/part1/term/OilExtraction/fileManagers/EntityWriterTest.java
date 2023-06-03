package ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.*;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EntityWriterTest {

    private static final String RESULT_FILENAME = "result.csv";
    private static final String EXPECTED_FILENAME = "expected.csv";
    private final Rig rig = new Rig(1, -23.5, 12.7, new HashSet<>());

    @Before
    public void setUp() throws IOException {
        Files.deleteIfExists(Path.of(RESULT_FILENAME));
    }

    @AfterAll
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(RESULT_FILENAME));
    }

    @Test
    public void testEmptyWrite() {
        EntityWriter.writeToCSV(null, RESULT_FILENAME);
        File file = new File(RESULT_FILENAME);
        assertFalse(file.exists());
    }

    @Test
    public void testWritingListOfRigs() throws IOException {

        EntityWriter.writeToCSV(rig, RESULT_FILENAME);
        Path expected = new File(RESULT_FILENAME).toPath();
        Path actual = new File(EXPECTED_FILENAME).toPath();

        List<String> expectedLines = Files.readAllLines(expected);
        List<String> actualLines = Files.readAllLines(actual);

        assertEquals(expectedLines, actualLines);
    }

    @Test
    public void testAlreadyExistingFile() throws IOException {
        File file = new File(RESULT_FILENAME);
        file.createNewFile();
        EntityWriter.writeToCSV(rig, RESULT_FILENAME);
        Path expected = new File(RESULT_FILENAME).toPath();
        Path actual = new File(EXPECTED_FILENAME).toPath();

        List<String> expectedLines = Files.readAllLines(expected);
        List<String> actualLines = Files.readAllLines(actual).subList(1, Files.readAllLines(actual).size());

        assertEquals(expectedLines, actualLines);
    }
}

