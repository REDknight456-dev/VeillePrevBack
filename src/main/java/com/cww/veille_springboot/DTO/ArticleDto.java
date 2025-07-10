package com.cww.veille_springboot.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDto {
    private Integer id; // Article's own ID
    private Integer numeroArticle;
    private String texte;
    private String exigence;
    private String risqueOpportunite;
    private String partiesInteressees;
    private String type; // "LOI" or "TERMEJURIDIQUE"
    private Integer parentId; // Use Integer for both Law and TermeJuridique

    // Default constructor
    public ArticleDto() {}

    // Full constructor
    public ArticleDto(Integer id, Integer numeroArticle, String texte, String exigence,
                      String risqueOpportunite, String partiesInteressees,
                      String type, Integer parentId) {
        this.id = id;
        this.numeroArticle = numeroArticle;
        this.texte = texte;
        this.exigence = exigence;
        this.risqueOpportunite = risqueOpportunite;
        this.partiesInteressees = partiesInteressees;
        this.type = type;
        this.parentId = parentId;
    }
}