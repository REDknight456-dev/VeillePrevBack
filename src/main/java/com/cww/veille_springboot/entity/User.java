package com.cww.veille_springboot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "utilisateur")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Integer id_utilisateur;
    private String nom;
    private String fonction_utilisateur;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Lob
    @Column(name = "photo_profil")
    private byte[] photo_profil;

    public enum Role {
        GESTIONNAIRE,
        ADMIN,
        NORMAL
    }
    private String numero_tel;


}
