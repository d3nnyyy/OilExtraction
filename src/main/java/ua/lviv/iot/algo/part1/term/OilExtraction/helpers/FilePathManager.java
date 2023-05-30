package ua.lviv.iot.algo.part1.term.OilExtraction.helpers;

import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FilePathManager {

    public static String getFileName(Entity entity) {
        final String entityName = entity.getClass().getSimpleName().toLowerCase();
        return "src/main/resources/entities/" + entityName + "-" + DateGetter.getDate() + ".csv";
    }

    public static List<String> getFilesCreatedInThisMonth(List<String> files) {

        String regex = "^\\w+-" + DateGetter.getMonth() + "-\\d{2}\\.csv$";
        Pattern pattern = Pattern.compile(regex);

        List<String> filteredFiles = new ArrayList<>();
        for (String file : files) {
            if (pattern.matcher(file).matches()) {
                filteredFiles.add(file);
            }
        }

        return filteredFiles;
    }
}

