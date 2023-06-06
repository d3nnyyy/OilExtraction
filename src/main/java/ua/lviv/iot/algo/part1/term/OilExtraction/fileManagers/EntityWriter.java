package ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers;

import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EntityWriter {
    public static final String LINE_SEPARATOR = System.lineSeparator();

    public static <T extends Entity> void writeToCSV(final T entity, final String path) {
        if (entity == null) {
            return;
        }
        try {
            boolean fileExists = new File(path).exists();

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), StandardCharsets.UTF_8))) {

                if (!fileExists) {
                    writer.write(entity.getHeaders() + LINE_SEPARATOR);
                }

                writer.write(entity.toCSV() + LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

