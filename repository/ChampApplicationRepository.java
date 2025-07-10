package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.ChampApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChampApplicationRepository extends JpaRepository<ChampApplication, Integer> {
    boolean existsByNom(String nom);
}
