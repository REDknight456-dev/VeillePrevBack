package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.entity.ChampApplication;
import com.cww.veille_springboot.repository.ChampApplicationRepository;
import com.cww.veille_springboot.service.ChampApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/champ-application")
public class ChampApplicationController {

    private final ChampApplicationRepository champApplicationRepository;
    private final ChampApplicationService champApplicationService;

    public ChampApplicationController(
            ChampApplicationRepository champApplicationRepository,
            ChampApplicationService champApplicationService
    ) {
        this.champApplicationRepository = champApplicationRepository;
        this.champApplicationService = champApplicationService;
    }

    // 📌 Récupérer toutes les champs d'application
    @GetMapping
    public ResponseEntity<List<ChampApplication>> getAllChampApplication() {
        List<ChampApplication> champs = champApplicationRepository.findAll();
        return ResponseEntity.ok(champs);
    }

    // 📌 Créer un nouveau champ d'application
    @PostMapping
    public ResponseEntity<ChampApplication> createChampApplication(@RequestBody ChampApplication champ) {
        if (champApplicationService.isChampApplicationNomExists(champ.getNom())) {
            return ResponseEntity.badRequest().build();
        }
        ChampApplication created = champApplicationRepository.save(champ);
        return ResponseEntity.status(201).body(created);
    }

    // 📌 Récupérer un champ par ID
    @GetMapping("/{id}")
    public ResponseEntity<ChampApplication> getChampApplicationById(@PathVariable Integer id) {
        return champApplicationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 📌 Mettre à jour un champ
    @PutMapping("/{id}")
    public ResponseEntity<ChampApplication> updateChampApplication(
            @PathVariable Integer id,
            @RequestBody ChampApplication champDetails
    ) {
        Optional<ChampApplication> existingChamp = champApplicationRepository.findById(id);

        if (existingChamp.isPresent()) {
            ChampApplication champ = existingChamp.get();
            champ.setNom(champDetails.getNom());
            champ.setId_champ_application(id); // ✅ Associer l'ID
            return ResponseEntity.ok(champApplicationRepository.save(champ));
        }
        return ResponseEntity.notFound().build();
    }


    // 📌 Supprimer un champ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChampApplication(@PathVariable Integer id) {
        if (champApplicationRepository.existsById(id)) {
            champApplicationRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
