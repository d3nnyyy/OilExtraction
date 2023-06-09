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

    private static final String PATH = "src/main/resources/entities/";
    private final RigService rigService;
    private final Map<Class<? extends Entity>, List<Entity>> entitiesMap;
    private final AtomicInteger idCounter;

    @Autowired
    public TankerService(final RigService rigService) {
        this.rigService = rigService;
        this.entitiesMap = EntityReader.readEntities(PATH);
        this.idCounter = new AtomicInteger(EntityReader.getLastId(Tanker.class, PATH));
    }


    public final List<? extends Entity> getTankers() {
        List<Entity> tankerList = entitiesMap.getOrDefault(Tanker.class, new ArrayList<>());
        for (Entity entity : tankerList) {
            if (entity instanceof Tanker tanker) {
                Rig rig = rigService.getRigById(tanker.getRigId());
                if (rig != null) {
                    tanker.setRig(rig);
                }
            }
        }
        return tankerList;
    }

    public final Tanker createTanker(final Tanker tanker) {
        String path = FilePathManager.getFileName(tanker);
        Rig rig = rigService.getRigById(tanker.getRigId());
        if (rig != null) {
            tanker.setRig(rig);
            tanker.setId(idCounter.incrementAndGet());
            tanker.setRigId(rig.getId());
            EntityWriter.writeToCSV(tanker, path);
            rig.getTankers().add(tanker);
            entitiesMap.computeIfAbsent(Tanker.class, k -> new LinkedList<>()).add(tanker);
            return tanker;
        } else {
            return null;
        }
    }

    public final Tanker getTankerById(final Integer id) {
        return (Tanker) entitiesMap.getOrDefault(
                        Tanker.class, new ArrayList<>())
                .stream()
                .filter(tanker -> tanker.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public final Tanker updateTanker(final Integer id, final Tanker tanker) {
        Tanker tankerFromDB = getTankerById(id);
        if (tankerFromDB != null) {
            tanker.setId(id);
            entitiesMap.get(Tanker.class).remove(tankerFromDB);
            entitiesMap.get(Tanker.class).add(tanker);

            Rig rigFromDB = rigService.getRigById(tankerFromDB.getRigId());
            Rig newRig = rigService.getRigById(tanker.getRigId());

            if (newRig == null) {
                return null;
            }

            if (rigFromDB != null) {
                rigFromDB.getTankers().remove(tankerFromDB);
            }

            newRig.getTankers().add(tanker);

            tanker.setRig(newRig);
            tanker.setRigId(newRig.getId());

            EntityReader.updateEntityInCsv(tanker, PATH);

            return tanker;
        } else {
            return null;
        }
    }

    public final boolean deleteTanker(final Integer id) {
        Tanker tanker = getTankerById(id);
        if (tanker != null) {
            entitiesMap.get(Tanker.class).remove(tanker);
            EntityReader.deleteEntityFromCSV(tanker, PATH);

            Rig rig = rigService.getRigById(tanker.getRigId());
            if (rig != null) {
                rig.getTankers().remove(tanker);
            }

            return true;
        } else {
            return false;
        }
    }

    public final List<? extends Entity> getFreeTankers() {
        List<Tanker> tankers = (List<Tanker>) getTankers();
        List<Tanker> freeTankers = new LinkedList<>();
        for (var tanker : tankers) {
            if (tanker.getRigId() == 0) {
                freeTankers.add(tanker);
            }
        }
        return freeTankers;
    }

}

