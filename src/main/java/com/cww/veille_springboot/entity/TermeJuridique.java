    package com.cww.veille_springboot.entity;

    import com.fasterxml.jackson.annotation.JsonCreator;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;
    import lombok.NoArgsConstructor;

    import java.util.Date;

    @Getter
    @Setter
    @NoArgsConstructor
    @Entity
    public class TermeJuridique {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        // Use ManyToOne for foreign key relationship to Loi
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_law", referencedColumnName = "id_law")
        private  Law law;

        @Column(name="serialNumber", nullable = false)
        private int serialNumber;

        public enum Type {
            DÉCRET, CODE, ARRÊTÉ, CIRCULAIRE ,Décret, Code, Arrêté, Circulaire,décret, code, arrêté, circulaire;

            @JsonCreator
            public static Type fromString(String value) {
                if (value == null) return null;
                return Type.valueOf(value.trim().toUpperCase());
            }
        }

        @Enumerated(EnumType.STRING)
        @Column(name="type",nullable = false)
        private Type type; // Now using enum for type
        @Column(name="source")
        private String source;
        @Column(name="name", nullable = false)
        private String name;
        @Column(name="resume")
        private String resume;
        @Column(name="title",nullable = false)
        private String title; // This will be set automatically by the service

        @Lob
        @Column(name = "file")
        private byte[] file;

        @Temporal(TemporalType.DATE)
        @Column(name="termeDate",nullable = false)
        private Date termeDate; // User-input date in yyyy/MM/dd format

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_theme", referencedColumnName = "id_theme")
        private Theme theme;
    }