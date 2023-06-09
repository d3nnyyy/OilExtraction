package ua.lviv.iot.algo.part1.term.OilExtraction.service;

import org.springframework.stereotype.Service;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityReader;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityWriter;
import ua.lviv.iot.algo.part1.term.OilExtraction.helpers.FilePathManager;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Tanker;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RigService {

    private final Map<Class<? extends Entity>, List<Entity>> entitiesMap;
    private final AtomicInteger idCounter;

    public RigService() {
        this.entitiesMap = EntityReader.readEntities();
        this.idCounter = new AtomicInteger(EntityReader.getLastId(Rig.class));
    }


    public List<? extends Entity> getRigs() {
        List<Entity> rigList = entitiesMap.getOrDefault(Rig.class, new ArrayList<>());
        for (Entity entity : rigList) {
            if (entity instanceof Rig rig) {
                Set<Tanker> tankerList = getTankersForRig(rig);
                rig.setTankers(tankerList);
            }
        }
        return rigList;
    }

    private Set<Tanker> getTankersForRig(Rig rig) {
        Set<Tanker> tankerList = new HashSet<>();
        List<Entity> tankerEntities = entitiesMap.getOrDefault(Tanker.class, new ArrayList<>());
        for (Entity entity : tankerEntities) {
            if (entity instanceof Tanker tanker && tanker.getRigId() == rig.getId()) {
                tankerList.add(tanker);
            }
        }
        return tankerList;
    }

    public final Rig createRig(final Rig rig) {
        rig.setId(idCounter.incrementAndGet());
        entitiesMap.computeIfAbsent(Rig.class, k -> new LinkedList<>()).add(rig);
        String path = FilePathManager.getFileName(rig);
        EntityWriter.writeToCSV(rig, path);
        return rig;
    }

    public final Rig getRigById(final Integer id) {
        return (Rig) entitiesMap
                .getOrDefault(Rig.class, new ArrayList<>())
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
