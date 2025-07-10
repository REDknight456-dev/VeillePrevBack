package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, Integer> {
    boolean existsByNom(String nom);
}
