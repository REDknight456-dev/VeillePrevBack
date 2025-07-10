package com.cww.veille_springboot.service;

import com.cww.veille_springboot.entity.ChampApplication;
import com.cww.veille_springboot.repository.ChampApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class ChampApplicationService {
    private final ChampApplicationRepository champApplicationRepository;

    public ChampApplicationService(ChampApplicationRepository champApplicationRepository) {
        this.champApplicationRepository = champApplicationRepository;
    }

    public boolean isChampApplicationNomExists(String nom) {
        return champApplicationRepository.findAll().stream()
                .anyMatch(champ -> champ.getNom().equals(nom));
    }
}
