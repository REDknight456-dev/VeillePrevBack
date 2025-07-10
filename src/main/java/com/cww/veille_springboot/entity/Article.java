package com.cww.veille_springboot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "article")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArticleLoi.class, name = "LOI"),
        @JsonSubTypes.Type(value = ArticleTermeJuridique.class, name = "TERMEJURIDIQUE")
})
public abstract class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",  updatable = false, nullable = false)
    private Integer id;

    @Column(name = "numeroArticle")
    private Integer numeroArticle;

    @Column(name = "texte", columnDefinition = "TEXT")
    private String texte;

    @Column(name = "exigence", columnDefinition = "TEXT")
    private String exigence;

    @Column(name = "risqueOpportunite", columnDefinition = "TEXT")
    private String risqueOpportunite;

    @Column(name = "partiesInteressees", columnDefinition = "TEXT")
    private String partiesInteressees;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Version> versions;

    public Article() {}
}