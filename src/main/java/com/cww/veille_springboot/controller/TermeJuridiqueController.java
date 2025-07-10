    package com.cww.veille_springboot.controller;
    
    import com.cww.veille_springboot.DTO.TermeJuridiqueDTO;
    import com.cww.veille_springboot.service.TermeJuridiqueService;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.List;
    
    @RestController
    @RequestMapping("/api/termes-juridiques")
    public class TermeJuridiqueController {
        private final TermeJuridiqueService service;
    
        public TermeJuridiqueController(TermeJuridiqueService service) {
            this.service = service;
        }
    
        @PostMapping
        public ResponseEntity<TermeJuridiqueDTO> create(@RequestBody TermeJuridiqueDTO dto) {
            return ResponseEntity.ok(service.create(dto));
        }
    
        @GetMapping
        public List<TermeJuridiqueDTO> getAll() {
            return service.findAll();
        }
    
        @GetMapping("/by-type/{type}")
        public List<TermeJuridiqueDTO> getByType(@PathVariable String type) {
            return service.findByType(type);
        }
    
        @GetMapping("/{id}")
        public ResponseEntity<TermeJuridiqueDTO> getById(@PathVariable Long id) {
            return service.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    
        @PutMapping("/{id}")
        public ResponseEntity<TermeJuridiqueDTO> update(@PathVariable Long id, @RequestBody TermeJuridiqueDTO dto) {
            return service.update(id, dto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
    
        // Search, filter, and paginate endpoint
        @GetMapping("/search")
        public Page<TermeJuridiqueDTO> search(
                @RequestParam(required = false) String q,
                @RequestParam(name = "id_theme", required = false) Integer themeId,
                @RequestParam(name = "id_law", required = false) Integer lawId,
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            Pageable pageable = PageRequest.of(page, size);
            return service.searchFilterPaginate(q, lawId, themeId, pageable);
        }
    
        @GetMapping("/code")
        public Page<TermeJuridiqueDTO> getAllCodeTerms(
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            List<TermeJuridiqueDTO> all = service.findByType("CODE");
            int start = Math.min(page * size, all.size());
            int end = Math.min(start + size, all.size());
            return new org.springframework.data.domain.PageImpl<>(all.subList(start, end), PageRequest.of(page, size), all.size());
        }
    
        @GetMapping("/code/search")
        public Page<TermeJuridiqueDTO> searchCodeTerms(
                @RequestParam(required = false) String q,
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            List<TermeJuridiqueDTO> filtered = service.findByType("CODE").stream()
                    .filter(t -> q == null || q.isEmpty() ||
                            (t.getName() != null && t.getName().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getTitle() != null && t.getTitle().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getResume() != null && t.getResume().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getSource() != null && t.getSource().toLowerCase().contains(q.toLowerCase()))
                    )
                    .collect(java.util.stream.Collectors.toList());
            int start = Math.min(page * size, filtered.size());
            int end = Math.min(start + size, filtered.size());
            return new org.springframework.data.domain.PageImpl<>(filtered.subList(start, end), PageRequest.of(page, size), filtered.size());
        }
    
        @GetMapping("/decret")
        public Page<TermeJuridiqueDTO> getAllDecretTerms(
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            List<TermeJuridiqueDTO> all = service.findByType("DECRET");
            int start = Math.min(page * size, all.size());
            int end = Math.min(start + size, all.size());
            return new org.springframework.data.domain.PageImpl<>(all.subList(start, end), PageRequest.of(page, size), all.size());
        }
    
        @GetMapping("/decret/search")
        public Page<TermeJuridiqueDTO> searchDecretTerms(
                @RequestParam(required = false) String q,
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            List<TermeJuridiqueDTO> filtered = service.findByType("DECRET").stream()
                    .filter(t -> q == null || q.isEmpty() ||
                            (t.getName() != null && t.getName().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getTitle() != null && t.getTitle().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getResume() != null && t.getResume().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getSource() != null && t.getSource().toLowerCase().contains(q.toLowerCase()))
                    )
                    .collect(java.util.stream.Collectors.toList());
            int start = Math.min(page * size, filtered.size());
            int end = Math.min(start + size, filtered.size());
            return new org.springframework.data.domain.PageImpl<>(filtered.subList(start, end), PageRequest.of(page, size), filtered.size());
        }
    
        @GetMapping("/circulaire")
        public Page<TermeJuridiqueDTO> getAllCirculaireTerms(
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            List<TermeJuridiqueDTO> all = service.findByType("CIRCULAIRE");
            int start = Math.min(page * size, all.size());
            int end = Math.min(start + size, all.size());
            return new org.springframework.data.domain.PageImpl<>(all.subList(start, end), PageRequest.of(page, size), all.size());
        }
    
        @GetMapping("/circulaire/search")
        public Page<TermeJuridiqueDTO> searchCirculaireTerms(
                @RequestParam(required = false) String q,
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            List<TermeJuridiqueDTO> filtered = service.findByType("CIRCULAIRE").stream()
                    .filter(t -> q == null || q.isEmpty() ||
                            (t.getName() != null && t.getName().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getTitle() != null && t.getTitle().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getResume() != null && t.getResume().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getSource() != null && t.getSource().toLowerCase().contains(q.toLowerCase()))
                    )
                    .collect(java.util.stream.Collectors.toList());
            int start = Math.min(page * size, filtered.size());
            int end = Math.min(start + size, filtered.size());
            return new org.springframework.data.domain.PageImpl<>(filtered.subList(start, end), PageRequest.of(page, size), filtered.size());
        }
    
        @GetMapping("/arrete")
        public Page<TermeJuridiqueDTO> getAllArreteTerms(
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            List<TermeJuridiqueDTO> all = service.findByType("ARRETE");
            int start = Math.min(page * size, all.size());
            int end = Math.min(start + size, all.size());
            return new org.springframework.data.domain.PageImpl<>(all.subList(start, end), PageRequest.of(page, size), all.size());
        }
    
        @GetMapping("/arrete/search")
        public Page<TermeJuridiqueDTO> searchArreteTerms(
                @RequestParam(required = false) String q,
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "10") int size
        ) {
            List<TermeJuridiqueDTO> filtered = service.findByType("ARRETE").stream()
                    .filter(t -> q == null || q.isEmpty() ||
                            (t.getName() != null && t.getName().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getTitle() != null && t.getTitle().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getResume() != null && t.getResume().toLowerCase().contains(q.toLowerCase())) ||
                            (t.getSource() != null && t.getSource().toLowerCase().contains(q.toLowerCase()))
                    )
                    .collect(java.util.stream.Collectors.toList());
            int start = Math.min(page * size, filtered.size());
            int end = Math.min(start + size, filtered.size());
            return new org.springframework.data.domain.PageImpl<>(filtered.subList(start, end), PageRequest.of(page, size), filtered.size());
        }
    }
