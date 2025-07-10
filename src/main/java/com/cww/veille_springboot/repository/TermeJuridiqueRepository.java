package com.cww.veille_springboot.repository;

import com.cww.veille_springboot.entity.TermeJuridique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TermeJuridiqueRepository extends JpaRepository<TermeJuridique, Long> {
    List<TermeJuridique> findByType(TermeJuridique.Type type);

    Optional<Object> findById(int termeId);
}