package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.entity.TexteReglementaire;
import com.cww.veille_springboot.entity.TextReglementaireVersion;
import com.cww.veille_springboot.service.TexteReglementaireService;
import com.cww.veille_springboot.service.TextReglementaireVersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/texteReglementaire")
public class TexteReglementaireController {

    private final TexteReglementaireService service;
    private final TextReglementaireVersionService versionService;

    public TexteReglementaireController(
            TexteReglementaireService service,
            TextReglementaireVersionService versionService) {
        this.service = service;
        this.versionService = versionService;
    }

    @GetMapping
    public ResponseEntity<Page<TexteReglementaire>> getAll(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TexteReglementaire> textes = service.searchTexteReglementaire(searchTerm, pageable);

        return ResponseEntity.ok(textes);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TexteReglementaire>> filterAndSearchTexteReglementaire(
            @RequestParam(required = false) String champApplication,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) String loiTitre,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TexteReglementaire> textes = service.filterAndSearchTexteReglementaire(champApplication, theme, loiTitre, searchTerm, pageable);

        return ResponseEntity.ok(textes);
    }



    @PostMapping
    public ResponseEntity<?> create(@RequestBody TexteReglementaire texte) {
        try {
            TexteReglementaire createdTexte = service.createTexteReglementaire(texte);
            versionService.createVersion(createdTexte);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTexte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TexteReglementaire> getById(@PathVariable Integer id) {
        Optional<TexteReglementaire> texte = service.getTexteReglementaireById(id);
        return texte.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody TexteReglementaire texteDetails) {
        try {
            TexteReglementaire updatedTexte = service.updateTexteReglementaire(id, texteDetails);
            return ResponseEntity.ok(updatedTexte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/plus")
    public ResponseEntity<?> updatePlus(@PathVariable Integer id, @RequestBody TexteReglementaire texteDetails) {
        try {
            TexteReglementaire updatedTexte = service.updateTexteReglementaire(id, texteDetails);
            versionService.createVersion(updatedTexte);
            return ResponseEntity.ok(updatedTexte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteTexteReglementaire(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<TextReglementaireVersion>> getVersions(@PathVariable Integer id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<TextReglementaireVersion> versions = versionService.getAllVersionsByTexteId(id);

        // Force l'initialisation des relations paresseuses
        /*for (TextReglementaireVersion version : versions) {
            version.getTexteReglementaire().getTexte(); // Accéder à un attribut pour forcer l'initialisation
        }*/

        return ResponseEntity.ok(versions);
    }

}
