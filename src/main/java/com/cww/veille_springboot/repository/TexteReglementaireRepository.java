package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.TexteReglementaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TexteReglementaireRepository extends JpaRepository<TexteReglementaire, Integer> {

    @Query("SELECT t FROM TexteReglementaire t WHERE " +
            "LOWER(t.loiTitre) LIKE %:searchTerm% OR " +
            "LOWER(t.codeNom) LIKE %:searchTerm% OR " +
            "LOWER(t.champApplication) LIKE %:searchTerm% OR " +
            "LOWER(t.theme) LIKE %:searchTerm% OR " +
            "LOWER(t.texteResume) LIKE %:searchTerm% OR " +
            "LOWER(t.texte) LIKE %:searchTerm% OR " +
            "(CAST(:searchTerm AS string) IS NOT NULL AND CAST(:searchTerm AS integer) = t.numeroArticle)")
    Page<TexteReglementaire> searchAllAttributes(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT t FROM TexteReglementaire t WHERE " +
            "(:champApplication IS NULL OR LOWER(t.champApplication) LIKE LOWER(:champApplication)) AND " +
            "(:theme IS NULL OR LOWER(t.theme) LIKE LOWER(:theme)) AND " +
            "(:loiTitre IS NULL OR LOWER(t.loiTitre) = LOWER(:loiTitre)) AND " +
            "(:searchTerm IS NULL OR (" +
            "   LOWER(t.loiTitre) LIKE %:searchTerm% OR " +
            "   LOWER(t.codeNom) LIKE %:searchTerm% OR " +
            "   LOWER(t.texteResume) LIKE %:searchTerm% OR " +
            "   LOWER(t.texte) LIKE %:searchTerm% OR " +
            "   (CAST(:searchTerm AS string) IS NOT NULL AND CAST(:searchTerm AS integer) = t.numeroArticle)" +
            "))")
    Page<TexteReglementaire> findByChampApplicationAndThemeAndLoiTitreAndSearchTerm(
            @Param("champApplication") String champApplication,
            @Param("theme") String theme,
            @Param("loiTitre") String loiTitre,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);


}
