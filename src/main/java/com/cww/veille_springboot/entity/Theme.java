    package com.cww.veille_springboot.entity;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Table(name = "theme", indexes = @Index(columnList = "nom", unique = true))
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public class Theme {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_theme")
        private Integer id_theme;

        @Column(name = "nom", length = 100, unique = true)
        private String nom;

        @Column(name = "nomChampApplication", length = 100, nullable = false)
        private String nomChampApplication;
    }
