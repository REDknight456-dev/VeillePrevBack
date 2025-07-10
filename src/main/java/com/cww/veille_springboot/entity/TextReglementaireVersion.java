package com.cww.veille_springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.cww.veille_springboot.config.Base64ByteArrayDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = "versionTexteReglementaire")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TextReglementaireVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_versionTexteReglementaire")
    private Integer idVersionTexteReglementaire;

    @Column(name = "id_texteReglementaire", nullable = false)
    private Integer idTexteReglementaire;

    @Column(name = "loi_titre", nullable = false)
    private String loiTitre;

    @Column(name = "theme", nullable = true) // Nullable car la colonne peut contenir des valeurs nulles
    private String theme;

    @Column(name = "code_nom", nullable = false)
    private String codeNom;

    @Column(name = "champ_application", nullable = true) // Nullable car la colonne peut contenir des valeurs nulles
    private String champApplication;

    @Column(name = "texte_resume", columnDefinition = "LONGTEXT")
    private String texteResume;

    @Column(name = "texte", columnDefinition = "LONGTEXT")
    private String texte;

    @Lob
    @Column(name = "piece_jointe", columnDefinition = "LONGBLOB")
    @JsonDeserialize(using = Base64ByteArrayDeserializer.class)
    private byte[] pieceJointe;

    @Column(name = "numero_article")
    private Integer numeroArticle;

    @Column(name = "version")
    private Integer version;

    // Relation avec TexteReglementaire (clé étrangère)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_texteReglementaire", referencedColumnName = "id_texteReglementaire", insertable = false, updatable = false)
    private TexteReglementaire texteReglementaire;

    // Relation avec Loi (clé étrangère)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loi_titre", referencedColumnName = "titre", insertable = false, updatable = false)
    private Loi loi;

    // Relation avec Code (clé étrangère)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_nom", referencedColumnName = "nom", insertable = false, updatable = false)
    private Code code;

    // Relation avec ChampApplication (clé étrangère)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "champ_application", referencedColumnName = "nom", insertable = false, updatable = false)
    private ChampApplication champApplicationEntity;

    // Relation avec Theme (clé étrangère)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme", referencedColumnName = "nom", insertable = false, updatable = false)
    private Theme themeEntity;
}
