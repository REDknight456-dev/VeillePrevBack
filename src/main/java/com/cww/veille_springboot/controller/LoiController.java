package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.entity.Loi;
import com.cww.veille_springboot.repository.LoiRepository;
import com.cww.veille_springboot.service.LoiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loi")
public class LoiController {

    private final LoiRepository loiRepository;
    private final LoiService loiService;

    public LoiController(LoiRepository loiRepository, LoiService loiService) {
        this.loiRepository = loiRepository;
        this.loiService = loiService;
    }

    // 📌 Récupérer toutes les lois
    @GetMapping
    public ResponseEntity<List<Loi>> getAllLoi() {
        List<Loi> lois = loiRepository.findAll();
        return ResponseEntity.ok(lois);
    }

    // 📌 Créer une nouvelle loi
    @PostMapping
    public ResponseEntity<Loi> createLoi(@RequestBody Loi loi) {
        // Générer le titre avant d’enregistrer
        loi.setTitre(loiService.generateTitre(loi));
        Loi loiCreated = loiRepository.save(loi);
        return ResponseEntity.status(201).body(loiCreated);
    }

    // 📌 Récupérer une loi par ID
    @GetMapping("/{id}")
    public ResponseEntity<Loi> getLoiById(@PathVariable Integer id) {
        return loiRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

        // 📌 Mettre à jour une loi
    @PutMapping("/{id}")
    public ResponseEntity<Loi> updateLoi(@PathVariable Integer id, @RequestBody Loi loiDetails) {
        Optional<Loi> loiOptional = loiRepository.findById(id);

        if (loiOptional.isPresent()) {
            Loi existingLoi = loiOptional.get();
            existingLoi.setNom(loiDetails.getNom());
            existingLoi.setType(loiDetails.getType());
            existingLoi.setNumero(loiDetails.getNumero());
            existingLoi.setDate(loiDetails.getDate());
            existingLoi.setSource(loiDetails.getSource());

            // Générer le titre automatiquement
            existingLoi.setTitre(loiService.generateTitre(existingLoi));

            Loi updatedLoi = loiRepository.save(existingLoi);
            return ResponseEntity.ok(updatedLoi);
        }
        return ResponseEntity.notFound().build();
    }

    // 📌 Supprimer une loi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoi(@PathVariable Integer id) {
        if (loiRepository.existsById(id)) {
            loiRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public LoiService getLoiService() {
        return loiService;
    }
}
