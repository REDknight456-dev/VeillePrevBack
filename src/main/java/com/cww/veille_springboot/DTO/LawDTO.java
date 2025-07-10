package com.cww.veille_springboot.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LawDTO {
    private Integer idLaw;
    private Integer idDomaine;
    private Integer idSousDomaine;
    private Integer numero;
    private LocalDate date;
    private String nom;
    private String source;
    private String resume;
    private String pJointe; // Base64
    private String titre;
}
    