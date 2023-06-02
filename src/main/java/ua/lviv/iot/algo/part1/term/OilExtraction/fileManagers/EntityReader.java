package ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers;


import org.springframework.stereotype.Component;
import ua.lviv.iot.algo.part1.term.OilExtraction.helpers.FilePathManager;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Tanker;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class EntityReader {

    private static final String PATH = "src/main/resources/entities/";

    public static Map<Class<? extends Entity>, List<Entity>> readEntities() {
        Map<Class<? extends Entity>, List<Entity>> entityMap = new HashMap<>();

        for (var csvFile : FilePathManager.getFilesCreatedInThisMonth(getFilesFromDirectory())) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(PATH + csvFile),
                            StandardCharsets.UTF_8))) {

                String line = br.readLine();

                if (line == null || line.isEmpty()) {
                    continue;
                }

                List<String> headers = Arrays.asList(line.split(";"));

                Class<? extends Entity> entityClass = getClassByCsvFile(csvFile);

                if (entityClass != null) {
                    List<Entity> entityList = new ArrayList<>();

                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(";");
                        if (values.length == headers.size()) {
                            Entity entity = createEntity(entityClass, headers, values);
                            entityList.add(entity);
                        }
                    }
                    entityMap.putIfAbsent(entityClass, new ArrayList<>());
                    entityMap.get(entityClass).addAll(entityList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return entityMap;
    }

    public static List<String> getFilesFromDirectory() {
        File folder = new File(EntityReader.PATH);
        String[] fileNames = folder.list();
        assert fileNames != null;
        return Arrays.asList(fileNames);
    }

    private static Class<? extends Entity> getClassByCsvFile(final String csvFile) {
        if (csvFile.contains("rig")) {
            return Rig.class;
        } else if (csvFile.contains("tanker")) {
            return Tanker.class;
        }
        return null;
    }

    private static Entity createEntity(
            final Class<? extends Entity> entityClass,
            final List<String> headers,
            final String[] values) {
        try {
            Constructor<? extends Entity> constructor = entityClass.getDeclaredConstructor();
            Entity entity = constructor.newInstance();

            Field[] fields = entityClass.getDeclaredFields();

            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                String value = values[i];

                Field field = getFieldByName(fields, header);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(entity, convertValueToFieldType(value, field.getType()));
                }
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Field getFieldByName(
            final Field[] fields,
            final String fileName) {
        for (Field field : fields) {
            if (field.getName().equals(fileName)) {
                return field;
            }
        }
        return null;
    }

    private static Object convertValueToFieldType(
            final String value,
            final Class<?> fieldType) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == List.class) {
            return Arrays.asList(value.split(","));
        }
        return value;
    }

    public static int getLastId(final Class<? extends Entity> entityClass) {
        List<String> files = getFilesFromDirectory();
        int maxId = 0;

        for (String file : files) {
            Class<? extends Entity> csvEntityClass = getClassByCsvFile(file);

            assert csvEntityClass != null;
            if (csvEntityClass.equals(entityClass)) {
                try (BufferedReader br = new BufferedReader(new FileReader(PATH + file))) {
                    String line;
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(";");
                        if (values.length > 0) {
                            int id = Integer.parseInt(values[0]);
                            if (id > maxId) {
                                maxId = id;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return maxId;
    }

    public static void deleteEntityFromCSV(final Entity entity) {
        List<String> files = getFilesFromDirectory();
        for (String file : files) {
            Class<? extends Entity> csvEntityClass = getClassByCsvFile(file);

            if (csvEntityClass == entity.getClass()) {
                Path path = Paths.get(PATH + file);
                List<String> lines;
                String headerLine;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8))) {

                    lines = new ArrayList<>();
                    headerLine = br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(";");
                        if (values.length > 0 && Integer.parseInt(values[0]) != entity.getId()) {
                            lines.add(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                overwriteCSV(path, lines, headerLine);
            }
        }
    }

    public static void updateEntityInCsv(final Entity entity) {
        List<String> files = getFilesFromDirectory();
        for (String file : files) {
            Class<? extends Entity> csvEntityClass = getClassByCsvFile(file);

            if (csvEntityClass == entity.getClass()) {
                Path path = Paths.get(PATH + file);
                List<String> lines;
                String headerLine;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8))) {
                    lines = new ArrayList<>();
                    headerLine = br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(";");
                        if (values.length > 0 && Integer.parseInt(values[0]) != entity.getId()) {
                            lines.add(line);
                        } else if (values.length > 0 && Integer.parseInt(values[0]) == entity.getId()) {

                            StringBuilder sb = new StringBuilder();

                            String[] headers = headerLine.split(";");
                            Field[] fields = entity.getClass().getDeclaredFields();
                            for (int i = 0; i < headers.length; i++) {
                                String header = headers[i];
                                for (Field field : fields) {
                                    field.setAccessible(true);
                                    if (field.getName().equals(header)) {
                                        Object value = field.get(entity);
                                        sb.append(value);
                                        if (i < headers.length - 1) {
                                            sb.append(";");
                                        }
                                        break;
                                    }
                                }
                            }


                            String updatedLine = sb.toString();
                            lines.add(updatedLine);

                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                overwriteCSV(path, lines, headerLine);
            }
        }
    }

    private static void overwriteCSV(final Path path,
                                     final List<String> lines,
                                     final String headerLine) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8))) {
            bw.write(headerLine);
            bw.newLine();
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

