package com.cww.veille_springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "article_loi")
@DiscriminatorValue("LOI")
@Getter
@Setter
public class ArticleLoi extends Article {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_law", referencedColumnName = "id_law", nullable = false)
    private Law law;

    public ArticleLoi() {
        super();
    }
}