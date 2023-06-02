package ua.lviv.iot.algo.part1.term.OilExtraction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Entity;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;
import ua.lviv.iot.algo.part1.term.OilExtraction.service.RigService;

import java.util.LinkedList;
import java.util.List;

@RequestMapping("/rigs")
@RestController
public class RigController {

    private final RigService rigService;

    @Autowired
    public RigController(final RigService rigService) {
        this.rigService = rigService;
    }

    @GetMapping
    public final List<? extends Entity> getRigs() {
        return new LinkedList<>(rigService.getRigs());
    }

    @GetMapping(path = "/{id}")
    public final Object getRigById(final @PathVariable("id") Integer id) {
        return rigService.getRigById(id) == null
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : rigService.getRigById(id);
    }

    @PostMapping(produces = {"application/json"})
    public final Rig createRig(final @RequestBody Rig rig) {
        return rigService.createRig(rig);
    }

    @PutMapping(path = "/{id}")
    public final ResponseEntity<Rig> updateRig(final @PathVariable("id") Integer id,
                                               final @RequestBody Rig rig) {
        Rig updatedRig = rigService.updateRig(id, rig);
        return updatedRig == null
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(updatedRig);
    }

    @DeleteMapping(path = "/{id}")
    public final ResponseEntity<Void> deleteRig(final @PathVariable("id") Integer id) {
        return rigService.deleteRig(id)
                ? ResponseEntity.status(HttpStatus.OK).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
