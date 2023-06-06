package ua.lviv.iot.algo.part1.term.OilExtraction.service;

import org.springframework.stereotype.Service;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityReader;
import ua.lviv.iot.algo.part1.term.OilExtraction.helpers.FilePathManager;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityWriter;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Tanker;

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

    public final List<? extends Entity> getRigs() {
        List<Entity> rigList = entitiesMap.get(Rig.class);
        if (rigList != null) {
            for (Entity entity : rigList) {
                if (entity instanceof Rig) {
                    Rig rig = (Rig) entity;
                    Set<Tanker> tankerList = new HashSet<>();
                    for (Entity tanker : entitiesMap.get(Tanker.class)) {
                        if (tanker instanceof Tanker) {
                            Tanker tanker1 = (Tanker) tanker;
                            if (tanker1.getRigId() == rig.getId()) {
                                tankerList.add(tanker1);
                            }
                        }
                    }
                    rig.setTankers(tankerList);
                }
            }
        }
        return rigList;
    }


    public final Rig createRig(final Rig rig) {
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

    public final Rig getRigById(final Integer id) {
        return (Rig) entitiesMap
                .get(Rig.class)
                .stream()
                .filter(rig -> rig.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public final Rig updateRig(final Integer id, final Rig rig) {
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

    public final boolean deleteRig(final Integer id) {
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
