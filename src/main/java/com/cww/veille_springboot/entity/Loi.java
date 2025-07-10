package com.cww.veille_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="loi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Loi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loi")
    private Integer id_loi;
    @Getter
    @Column(name = "nom", nullable = false)
    private String nom;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "numero", nullable = false)
    private Integer numero;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "source")
    private String source;
    @Column(name = "titre")
    private String titre;

}
