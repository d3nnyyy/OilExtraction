package ua.lviv.iot.algo.part1.term.OilExtraction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Tanker;
import ua.lviv.iot.algo.part1.term.OilExtraction.service.TankerService;

import java.util.List;

@RestController
@RequestMapping("/tankers")
public class TankerController {

    private final TankerService tankerService;

    @Autowired
    public TankerController(TankerService tankerService) {
        this.tankerService = tankerService;
    }

    @GetMapping
    public List<? extends Entity> getTankers() {
        return tankerService.getTankers();
    }

    @GetMapping(path = "/{id}")
    public Object getTankerById(final @PathVariable("id") Integer id) {
        return tankerService.getTankerById(id) == null
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : tankerService.getTankerById(id);
    }

    @GetMapping(path = "/free")
    public List<? extends Entity> getFreeTankers() {
        return tankerService.getFreeTankers();
    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity<Tanker> createTanker(final @RequestBody Tanker tanker) {
        Tanker createdTanker = tankerService.createTanker(tanker);
        if (createdTanker != null) {
            return ResponseEntity.ok(createdTanker);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Tanker> updateTanker(final @PathVariable("id") Integer id, final @RequestBody Tanker tanker) {
        Tanker updatedTanker = tankerService.updateTanker(id, tanker);
        return updatedTanker == null
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(updatedTanker);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTanker(final @PathVariable("id") Integer id) {
        return tankerService.deleteTanker(id)
                ? ResponseEntity.status(HttpStatus.OK).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
