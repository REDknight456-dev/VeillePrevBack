package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.DTO.LawDTO;
import com.cww.veille_springboot.entity.Law;
import com.cww.veille_springboot.repository.LawRepository;
import com.cww.veille_springboot.service.LawService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/law")
public class LawController {

    private final LawRepository lawRepository;
    private final LawService lawService;

    public LawController(LawRepository lawRepository, LawService lawService) {
        this.lawRepository = lawRepository;
        this.lawService = lawService;
    }

    // Get all laws (not paginated)
    @GetMapping
    public ResponseEntity<List<LawDTO>> getAllLaws() {
        List<LawDTO> laws = lawService.toDTOList(lawRepository.findAll());
        return ResponseEntity.ok(laws);
    }

    // Search, filter, and paginate laws
    @GetMapping("/search")
    public ResponseEntity<Page<LawDTO>> searchLaws(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer idDomaine,
            @RequestParam(required = false) Integer idSousDomaine,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Law> lawPage = lawRepository.searchAndFilter(
                (search == null || search.isBlank()) ? null : search,
                idDomaine,
                idSousDomaine,
                pageable
        );
        Page<LawDTO> dtoPage = lawPage.map(lawService::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    // Create law
    @PostMapping
    public ResponseEntity<LawDTO> createLaw(@RequestBody LawDTO dto) {
        Law law = lawService.fromDTO(dto);
        law.setTitre(lawService.generateTitre(law));
        Law saved = lawRepository.save(law);
        return ResponseEntity.status(201).body(lawService.toDTO(saved));
    }

    // Get law by ID
    @GetMapping("/{id}")
    public ResponseEntity<LawDTO> getLawById(@PathVariable Integer id) {
        return lawRepository.findById(id)
                .map(law -> ResponseEntity.ok(lawService.toDTO(law)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Update law
    @PutMapping("/{id}")
    public ResponseEntity<LawDTO> updateLaw(@PathVariable Integer id, @RequestBody LawDTO dto) {
        return lawRepository.findById(id)
                .map(existing -> {
                    Law updated = lawService.fromDTO(dto);
                    updated.setIdLaw(existing.getIdLaw()); // preserve ID
                    updated.setTitre(lawService.generateTitre(updated));
                    Law saved = lawRepository.save(updated);
                    return ResponseEntity.ok(lawService.toDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete law
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaw(@PathVariable Integer id) {
        if (lawRepository.existsById(id)) {
            lawRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}