package com.cww.veille_springboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "versionArticle")
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArticle", nullable = false)
    @JsonBackReference
    private Article article;

    @Column(name = "texte", columnDefinition = "TEXT")
    private String texte;
    @Column(name = "numeroArticle")
    private int numeroArticle;

    @Column(name = "exigence", columnDefinition = "TEXT")
    private String exigence;

    @Column(name = "risqueOpportunite", columnDefinition = "TEXT")
    private String risqueOpportunite;

    @Column(name = "partiesInteressees", columnDefinition = "TEXT")
    private String partiesInteressees;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}