package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.Loi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoiRepository extends JpaRepository<Loi, Integer> {
    boolean existsByTitre(String titre);
}
