package com.naturegram.api.controller;

import com.naturegram.api.domain.Observation;
import com.naturegram.api.repository.ObservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/observations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ObservationController {

    private final ObservationRepository observationRepository;

    @GetMapping
    public ResponseEntity<List<Observation>> getAllObservations() {
        List<Observation> observations = observationRepository.findAll();
        return ResponseEntity.ok(observations);
    }

    @PostMapping
    public ResponseEntity<Observation> createObservation(@RequestBody Observation observation) {
        Observation saved = observationRepository.save(observation);
        return ResponseEntity.ok(saved);
    }
}
