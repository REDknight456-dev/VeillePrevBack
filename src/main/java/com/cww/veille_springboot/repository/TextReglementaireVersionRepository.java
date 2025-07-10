package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.TextReglementaireVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TextReglementaireVersionRepository extends JpaRepository<TextReglementaireVersion, Integer> {
    List<TextReglementaireVersion> findByIdTexteReglementaire(Integer texteReglementaireId);
    
    @Modifying
    @Query("DELETE FROM TextReglementaireVersion v WHERE v.idTexteReglementaire = :texteId")
    void deleteAllByTexteReglementaireId(@Param("texteId") Integer texteId);
}
