package com.cww.veille_springboot.controller;

import com.cww.veille_springboot.entity.Version;
import com.cww.veille_springboot.entity.Article;
import com.cww.veille_springboot.repository.VersionRepository;
import com.cww.veille_springboot.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/versions")
public class VersionController {

    @Autowired
    private VersionRepository versionRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<Version>> getVersionsByArticle(@PathVariable Integer articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        return ResponseEntity.ok(article.getVersions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Version> getVersion(@PathVariable Integer id) {
        Version version = versionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));
        return ResponseEntity.ok(version);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVersion(@PathVariable Integer id) {
        if (!versionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        versionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

