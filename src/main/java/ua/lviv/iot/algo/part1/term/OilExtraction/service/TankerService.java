package ua.lviv.iot.algo.part1.term.OilExtraction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityReader;
import ua.lviv.iot.algo.part1.term.OilExtraction.helpers.FilePathManager;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Tanker;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityWriter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TankerService {

    private final RigService rigService;

    @Autowired
    public TankerService(RigService rigService) {
        this.rigService = rigService;
        this.entitiesMap = EntityReader.readEntities();
    }

    private final Map<Integer, Tanker> tankers = new HashMap<>();
    private final Map<Class<? extends Entity>, List<Entity>> entitiesMap;
    private final AtomicInteger idCounter = new AtomicInteger(
            EntityReader.getLastId(Tanker.class)
    );

    public List<? extends Entity> getTankers() {
        return new LinkedList<>(entitiesMap.get(Tanker.class));

    }

    public Tanker createTanker(final Tanker tanker) {
        String path = FilePathManager.getFileName(tanker);
        Rig rig = rigService.getRigById(tanker.getRigId());
        if (rig != null) {
            tanker.setRig(rig);
            tanker.setId(idCounter.incrementAndGet());
            tanker.setRigId(rig.getId());
            tankers.put(tanker.getId(), tanker);
            EntityWriter.writeToCSV(tanker, path);
            rig.getTankers().add(tanker);
            if (!entitiesMap.containsKey(Tanker.class)) {
                entitiesMap.put(Tanker.class, new LinkedList<>());
            } else {
                entitiesMap.get(Tanker.class).add(tanker);
            }
            return tanker;
        } else {
            return null;
        }
    }

    public Tanker getTankerById(Integer id) {
        return (Tanker) entitiesMap
                .get(Tanker.class)
                .stream()
                .filter(tanker -> tanker.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Tanker updateTanker(Integer id, Tanker tanker) {
        if (tankers.containsKey(id)) {
            tanker.setId(id);
            tankers.put(id, tanker);
            return tanker;
        } else {
            return null;
        }
    }

    public boolean deleteTanker(Integer id) {
        Tanker tanker = getTankerById(id);
        if (tanker!= null) {
            tankers.remove(id);
            entitiesMap.get(Tanker.class).remove(tanker);
            EntityReader.deleteEntityFromCSV(tanker);
            return true;
        } else {
            return false;
        }
    }

}
