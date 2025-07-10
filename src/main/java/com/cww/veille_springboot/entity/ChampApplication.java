package com.cww.veille_springboot.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="champ_application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChampApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_champ_application")
    private Integer id_champ_application;

    @Column(name = "nom") // ✅ Vérifiez aussi ce nom
    private String nom;
}
