package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.entity.Code;
import com.cww.veille_springboot.repository.CodeRepository;
import com.cww.veille_springboot.service.CodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/code")
public class CodeController {

    private final CodeRepository codeRepository;
    private final CodeService codeService;

    public CodeController(CodeRepository codeRepository, CodeService codeService) {
        this.codeRepository = codeRepository;
        this.codeService = codeService;
    }

    // 📌 Récupérer tous les codes
    @GetMapping
    public ResponseEntity<List<Code>> getAllCodes() {
        List<Code> codes = codeRepository.findAll();
        return ResponseEntity.ok(codes);
    }

    // 📌 Créer un nouveau code
    @PostMapping
    public ResponseEntity<?> createCode(@RequestBody Code code) {
        // Vérifier si le nom_loi existe dans la table loi
        if (!codeService.isLoiNomExists(code.getNom_loi())) {
            return ResponseEntity.badRequest().body("La loi avec le nom spécifié n'existe pas");
        }

        Code codeCreated = codeRepository.save(code);
        return ResponseEntity.status(201).body(codeCreated);
    }

    // 📌 Récupérer un code par ID
    @GetMapping("/{id}")
    public ResponseEntity<Code> getCodeById(@PathVariable Integer id) {
        return codeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 📌 Mettre à jour un code
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCode(@PathVariable Integer id, @RequestBody Code codeDetails) {
        Optional<Code> codeOptional = codeRepository.findById(id);

        if (codeOptional.isPresent()) {
            // Vérifier si le nouveau nom_loi existe dans la table loi
            if (!codeService.isLoiNomExists(codeDetails.getNom_loi())) {
                return ResponseEntity.badRequest().body("La loi avec le nom spécifié n'existe pas");
            }

            Code existingCode = codeOptional.get();
            existingCode.setNom(codeDetails.getNom());
            existingCode.setNom_loi(codeDetails.getNom_loi());

            Code updatedCode = codeRepository.save(existingCode);
            return ResponseEntity.ok(updatedCode);
        }
        return ResponseEntity.notFound().build();
    }

    // 📌 Supprimer un code
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCode(@PathVariable Integer id) {
        if (codeRepository.existsById(id)) {
            codeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}