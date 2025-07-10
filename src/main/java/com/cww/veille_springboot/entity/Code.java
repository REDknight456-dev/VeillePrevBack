package com.cww.veille_springboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_code")
    private Integer id_code;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "nom_loi", nullable = false)
    private String nom_loi;

    @ManyToOne
    @JoinColumn(name = "nom_loi", referencedColumnName = "nom", insertable = false, updatable = false)
    private Loi loi;
}