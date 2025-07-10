package com.cww.veille_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "article_termejuridique")
@DiscriminatorValue("TERMEJURIDIQUE")
@Getter
@Setter
public class ArticleTermeJuridique extends Article {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_termejuridique", referencedColumnName = "id", nullable = false)
    private TermeJuridique termeJuridique;

    public ArticleTermeJuridique() {
        super();
    }
}