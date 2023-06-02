package ua.lviv.iot.algo.part1.term.OilExtraction.service;

import org.springframework.stereotype.Service;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityReader;
import ua.lviv.iot.algo.part1.term.OilExtraction.helpers.FilePathManager;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityWriter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RigService {

    private final Map<Class<? extends Entity>, List<Entity>> entitiesMap;

    public RigService() {
        this.entitiesMap = EntityReader.readEntities();
    }

    private final AtomicInteger idCounter =
            new AtomicInteger(EntityReader.getLastId(Rig.class));

    public List<? extends Entity> getRigs() {
        return new LinkedList<>(entitiesMap.get(Rig.class));
    }

    public Rig createRig(final Rig rig) {
        rig.setId(idCounter.incrementAndGet());
        if (!entitiesMap.containsKey(Rig.class)) {
            entitiesMap.put(Rig.class, new LinkedList<>());
        } else {
            entitiesMap.get(Rig.class).add(rig);
        }
        String path = FilePathManager.getFileName(rig);
        EntityWriter.writeToCSV(rig, path);
        return rig;
    }

    public Rig getRigById(Integer id) {
        return (Rig) entitiesMap
                .get(Rig.class)
                .stream()
                .filter(rig -> rig.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Rig updateRig(Integer id, Rig rig) {
        Rig rigFromDB = getRigById(id);
        if (rigFromDB != null) {
            rig.setId(id);
            entitiesMap.get(Rig.class).remove(rigFromDB);
            entitiesMap.get(Rig.class).add(rig);
            EntityReader.updateEntityInCsv(rig);
            return rig;
        } else {
            return null;
        }
    }

    public boolean deleteRig(Integer id) {
        Rig rig = getRigById(id);
        if (rig != null) {
            entitiesMap.get(Rig.class).remove(rig);
            EntityReader.deleteEntityFromCSV(rig);
            return true;
        } else {
            return false;
        }
    }
}
