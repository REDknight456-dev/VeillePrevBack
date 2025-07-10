package com.cww.veille_springboot.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Law")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Law {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_law")
    private Integer idLaw;

    @Column(name = "idDomaine", nullable = false)
    private Integer idDomaine;

    @Column(name = "idSousDomaine", nullable = false)
    private Integer idSousDomaine;

    @Column(name = "numLoi", nullable = false)
    private Integer numero;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "nomLoi", nullable = false)
    private String nom;

    @Column(name = "source")
    private String source;

    @Column(name = "resume")
    private String resume;

    @Lob
    @Column(name = "pJointe")
    private byte[] pJointe;

    @Column(name = "titre")
    private String titre;
}
