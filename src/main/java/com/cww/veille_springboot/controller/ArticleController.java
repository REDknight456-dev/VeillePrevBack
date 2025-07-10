package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.DTO.ArticleDto;
import com.cww.veille_springboot.entity.*;
import com.cww.veille_springboot.repository.ArticleRepository;
import com.cww.veille_springboot.repository.LawRepository;
import com.cww.veille_springboot.repository.TermeJuridiqueRepository;
import com.cww.veille_springboot.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private LawRepository lawRepository;
    @Autowired
    private TermeJuridiqueRepository termeJuridiqueRepository;
    @Autowired
    private VersionRepository versionRepository;

    @PostMapping
    public ArticleDto createArticle(@RequestBody ArticleDto dto) {
        Article article;
        if ("LOI".equalsIgnoreCase(dto.getType())) {
            ArticleLoi articleLoi = new ArticleLoi();
            articleLoi.setNumeroArticle(dto.getNumeroArticle());
            articleLoi.setTexte(dto.getTexte());
            articleLoi.setExigence(dto.getExigence());
            articleLoi.setRisqueOpportunite(dto.getRisqueOpportunite());
            articleLoi.setPartiesInteressees(dto.getPartiesInteressees());

            if (dto.getParentId() == null) {
                throw new IllegalArgumentException("Law ID (parentId) must be provided for LOI type");
            }
            Integer lawId = dto.getParentId();
            Law law = lawRepository.findById(lawId)
                    .orElseThrow(() -> new IllegalArgumentException("Law not found with id: " + lawId));
            articleLoi.setLaw(law);
            article = articleLoi;
        } else if ("TERMEJURIDIQUE".equalsIgnoreCase(dto.getType())) {
            ArticleTermeJuridique articleTJ = new ArticleTermeJuridique();
            articleTJ.setNumeroArticle(dto.getNumeroArticle());
            articleTJ.setTexte(dto.getTexte());
            articleTJ.setExigence(dto.getExigence());
            articleTJ.setRisqueOpportunite(dto.getRisqueOpportunite());
            articleTJ.setPartiesInteressees(dto.getPartiesInteressees());

            if (dto.getParentId() == null) {
                throw new IllegalArgumentException("TermeJuridique ID (parentId) must be provided for TERMEJURIDIQUE type");
            }
            Integer termeId = dto.getParentId();
            TermeJuridique terme = (TermeJuridique) termeJuridiqueRepository.findById(termeId)
                    .orElseThrow(() -> new IllegalArgumentException("TermeJuridique not found with id: " + termeId));
            articleTJ.setTermeJuridique(terme);
            article = articleTJ;
        } else {
            throw new IllegalArgumentException("Type must be LOI or TERMEJURIDIQUE");
        }
        Article saved = articleRepository.save(article);
        return mapToDto(saved);
    }

    @GetMapping("/{id}")
    public ArticleDto getArticle(@PathVariable Integer id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        return mapToDto(article);
    }

    @GetMapping
    public List<ArticleDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Integer id) {
        if (!articleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        articleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/versions")
    public ResponseEntity<?> createVersion(@PathVariable Integer id, @RequestBody Version version) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        // Set the article for the version
        version.setArticle(article);
        Version savedVersion = versionRepository.save(version);

        // Update the article fields with the new version's data (example: texte, exigence, etc.)
        article.setTexte(version.getTexte());
        article.setExigence(version.getExigence());
        article.setRisqueOpportunite(version.getRisqueOpportunite());
        article.setPartiesInteressees(version.getPartiesInteressees());
        articleRepository.save(article);

        return ResponseEntity.ok(savedVersion);
    }

    // --- GENERAL SEARCH AND FILTER FOR ARTICLES ---
    @GetMapping("/search")
    public ResponseEntity<?> searchArticles(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer numeroArticle,
            @RequestParam(required = false) String texte,
            @RequestParam(required = false) String exigence,
            @RequestParam(required = false) String risqueOpportunite,
            @RequestParam(required = false) String partiesInteressees,
            @RequestParam(required = false) Integer parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Article> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // General search on multiple fields
            if (q != null && !q.isEmpty()) {
                String pattern = "%" + q.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("texte")), pattern),
                        cb.like(cb.lower(root.get("exigence")), pattern),
                        cb.like(cb.lower(root.get("risqueOpportunite")), pattern),
                        cb.like(cb.lower(root.get("partiesInteressees")), pattern)
                ));
            }
            // Filter by type
            if (type != null && !type.isEmpty()) {
                if ("loi".equalsIgnoreCase(type)) {
                    predicates.add(cb.equal(root.type(), ArticleLoi.class));
                } else if ("termejuridique".equalsIgnoreCase(type)) {
                    predicates.add(cb.equal(root.type(), ArticleTermeJuridique.class));
                }
            }
            if (numeroArticle != null) {
                predicates.add(cb.equal(root.get("numeroArticle"), numeroArticle));
            }
            if (texte != null && !texte.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("texte")), "%" + texte.toLowerCase() + "%"));
            }
            if (exigence != null && !exigence.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("exigence")), "%" + exigence.toLowerCase() + "%"));
            }
            if (risqueOpportunite != null && !risqueOpportunite.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("risqueOpportunite")), "%" + risqueOpportunite.toLowerCase() + "%"));
            }
            if (partiesInteressees != null && !partiesInteressees.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("partiesInteressees")), "%" + partiesInteressees.toLowerCase() + "%"));
            }
            // Filtering by parentId (for LOI or TERMEJURIDIQUE)
            if (parentId != null) {
                Predicate parentPredicate = cb.disjunction();
                parentPredicate = cb.or(
                        cb.and(
                                cb.equal(root.type(), ArticleLoi.class),
                                cb.equal(root.get("law").get("idLaw"), parentId)
                        ),
                        cb.and(
                                cb.equal(root.type(), ArticleTermeJuridique.class),
                                cb.equal(root.get("termeJuridique").get("id"), parentId)
                        )
                );
                predicates.add(parentPredicate);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Article> articlePage = articleRepository.findAll(spec, pageable);
        Page<ArticleDto> dtoPage = articlePage.map(this::mapToDto);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/loi")
    public Page<ArticleDto> getAllLoiArticles(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<ArticleDto> all = articleRepository.findAll().stream()
                .filter(a -> a instanceof ArticleLoi)
                .map(this::mapToDto)
                .collect(Collectors.toList());
        int start = Math.min(page * size, all.size());
        int end = Math.min(start + size, all.size());
        return new PageImpl<>(all.subList(start, end), PageRequest.of(page, size), all.size());
    }

    @GetMapping("/loi/search")
    public Page<ArticleDto> searchLoiArticles(
            @RequestParam(required = false) String q,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<ArticleDto> filtered = articleRepository.findAll().stream()
                .filter(a -> a instanceof ArticleLoi)
                .filter(a -> q == null || q.isEmpty() ||
                        (a.getTexte() != null && a.getTexte().toLowerCase().contains(q.toLowerCase())) ||
                        (a.getExigence() != null && a.getExigence().toLowerCase().contains(q.toLowerCase())) ||
                        (a.getRisqueOpportunite() != null && a.getRisqueOpportunite().toLowerCase().contains(q.toLowerCase())) ||
                        (a.getPartiesInteressees() != null && a.getPartiesInteressees().toLowerCase().contains(q.toLowerCase()))
                )
                .map(this::mapToDto)
                .collect(Collectors.toList());
        int start = Math.min(page * size, filtered.size());
        int end = Math.min(start + size, filtered.size());
        return new PageImpl<>(filtered.subList(start, end), PageRequest.of(page, size), filtered.size());
    }

    @GetMapping("/termejuridique")
    public Page<ArticleDto> getAllTermeJuridiqueArticles(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<ArticleDto> all = articleRepository.findAll().stream()
                .filter(a -> a instanceof ArticleTermeJuridique)
                .map(this::mapToDto)
                .collect(Collectors.toList());
        int start = Math.min(page * size, all.size());
        int end = Math.min(start + size, all.size());
        return new PageImpl<>(all.subList(start, end), PageRequest.of(page, size), all.size());
    }

    @GetMapping("/termejuridique/search")
    public Page<ArticleDto> searchTermeJuridiqueArticles(
            @RequestParam(required = false) String q,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<ArticleDto> filtered = articleRepository.findAll().stream()
                .filter(a -> a instanceof ArticleTermeJuridique)
                .filter(a -> q == null || q.isEmpty() ||
                        (a.getTexte() != null && a.getTexte().toLowerCase().contains(q.toLowerCase())) ||
                        (a.getExigence() != null && a.getExigence().toLowerCase().contains(q.toLowerCase())) ||
                        (a.getRisqueOpportunite() != null && a.getRisqueOpportunite().toLowerCase().contains(q.toLowerCase())) ||
                        (a.getPartiesInteressees() != null && a.getPartiesInteressees().toLowerCase().contains(q.toLowerCase()))
                )
                .map(this::mapToDto)
                .collect(Collectors.toList());
        int start = Math.min(page * size, filtered.size());
        int end = Math.min(start + size, filtered.size());
        return new PageImpl<>(filtered.subList(start, end), PageRequest.of(page, size), filtered.size());
    }

    // Utility method for entity->DTO mapping
    private ArticleDto mapToDto(Article article) {
        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setNumeroArticle(article.getNumeroArticle());
        dto.setTexte(article.getTexte());
        dto.setExigence(article.getExigence());
        dto.setRisqueOpportunite(article.getRisqueOpportunite());
        dto.setPartiesInteressees(article.getPartiesInteressees());
        if (article instanceof ArticleLoi) {
            dto.setType("LOI");
            Law law = ((ArticleLoi) article).getLaw();
            if (law != null && law.getIdLaw() != null) {
                dto.setParentId(law.getIdLaw());
            }
        } else if (article instanceof ArticleTermeJuridique) {
            dto.setType("TERMEJURIDIQUE");
            TermeJuridique terme = ((ArticleTermeJuridique) article).getTermeJuridique();
            if (terme != null && terme.getId() != 0 && terme.getId() != 0) {
                dto.setParentId(terme.getId());
            }
        }
        return dto;
    }
}