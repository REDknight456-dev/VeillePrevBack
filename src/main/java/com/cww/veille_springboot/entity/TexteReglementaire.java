package com.cww.veille_springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.cww.veille_springboot.config.Base64ByteArrayDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = "texteReglementaire")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TexteReglementaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_texteReglementaire")
    private Integer idTexteReglementaire;

    @Column(name = "champ_application", nullable = false)
    private String champApplication;

    @Column(name = "code_nom", nullable = false)
    private String codeNom;

    @Column(name = "loi_titre", nullable = false)
    private String loiTitre;

    @Column(name = "theme")
    private String theme;

    @Column(name = "numero_article")
    private Integer numeroArticle;

    @Lob
    @Column(name = "piece_jointe", columnDefinition = "LONGBLOB")
    @JsonDeserialize(using = Base64ByteArrayDeserializer.class)
    private byte[] pieceJointe;

    @Column(name = "texte", columnDefinition = "LONGTEXT")
    private String texte;

    @Column(name = "texte_resume", columnDefinition = "LONGTEXT")
    private String texteResume;

    @Column(name = "version")
    private Integer version;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "champ_application", referencedColumnName = "nom", insertable = false, updatable = false)
    private ChampApplication champApplicationEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme", referencedColumnName = "nom", insertable = false, updatable = false)
    private Theme themeEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loi_titre", referencedColumnName = "titre", insertable = false, updatable = false)
    private Loi loi;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_nom", referencedColumnName = "nom", insertable = false, updatable = false)
    private Code code;
}
