package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Integer> {

    List<Theme> findByNomChampApplication(@Param("nomChampApplication") String nomChampApplication);

    boolean existsByNom(String nom);
}
