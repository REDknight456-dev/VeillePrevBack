package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.Law;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LawRepository extends JpaRepository<Law, Integer> {
    boolean existsByTitre(String titre);

    @Query("SELECT l FROM Law l WHERE " +
            "(:search IS NULL OR " +
            "LOWER(l.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.source) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.resume) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.titre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "CAST(l.numero AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(l.idLaw AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(l.date AS string) LIKE CONCAT('%', :search, '%')) AND " +
            "(:idDomaine IS NULL OR l.idDomaine = :idDomaine) AND " +
            "(:idSousDomaine IS NULL OR l.idSousDomaine = :idSousDomaine)")
    Page<Law> searchAndFilter(
            @Param("search") String search,
            @Param("idDomaine") Integer idDomaine,
            @Param("idSousDomaine") Integer idSousDomaine,
            Pageable pageable
    );
}