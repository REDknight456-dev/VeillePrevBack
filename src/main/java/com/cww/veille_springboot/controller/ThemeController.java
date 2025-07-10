package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.entity.Theme;
import com.cww.veille_springboot.repository.ThemeRepository;
import com.cww.veille_springboot.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeRepository themeRepository;
    private final ThemeService themeService;

    public ThemeController(
            ThemeRepository themeRepository,
            ThemeService themeService
    ) {
        this.themeRepository = themeRepository;
        this.themeService = themeService;
    }

    // 📌 Lister tous les thèmes
    @GetMapping
    public ResponseEntity<List<Theme>> getAllThemes() {
        return ResponseEntity.ok(themeRepository.findAll());
    }

    // 📌 Créer un thème 
    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        if (!themeService.isThemeNomUnique(theme.getNom())) {
            return ResponseEntity.badRequest().build();
        }
        if (!themeService.champApplicationExists(theme.getNomChampApplication())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(themeRepository.save(theme));
    }

    // 📌 Récupérer par ID
    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Integer id) {
        return themeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 📌 Mettre à jour un thème
    @PutMapping("/{id}")
    public ResponseEntity<Theme> updateTheme(
            @PathVariable Integer id,
            @RequestBody Theme themeDetails
    ) {
        Optional<Theme> themeOpt = themeRepository.findById(id);

        if (themeOpt.isPresent()) {
            Theme theme = themeOpt.get();

            if (!theme.getNom().equals(themeDetails.getNom())
                    && !themeService.isThemeNomUnique(themeDetails.getNom())) {
                return ResponseEntity.badRequest().build();
            }

            if (!themeService.champApplicationExists(themeDetails.getNomChampApplication())) {
                return ResponseEntity.badRequest().build();
            }

            theme.setNom(themeDetails.getNom());
            theme.setNomChampApplication(themeDetails.getNomChampApplication());
            return ResponseEntity.ok(themeRepository.save(theme));
        }
        return ResponseEntity.notFound().build();
    }

    // 📌 Supprimer un thème
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Integer id) {
        if (themeRepository.existsById(id)) {
            themeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/champApplication/{champApplication}/themes")
    public ResponseEntity<List<Theme>> getThemesByChampApplication(@PathVariable String champApplication) {
        List<Theme> themes = themeService.getThemesByChampApplication(champApplication);
        return ResponseEntity.ok(themes);
    }
}
