package com.cww.veille_springboot.service;

import com.cww.veille_springboot.entity.Theme;
import com.cww.veille_springboot.repository.ChampApplicationRepository;
import com.cww.veille_springboot.repository.ThemeRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ChampApplicationRepository champApplicationRepository;

    public ThemeService(
            ThemeRepository themeRepository,
            ChampApplicationRepository champApplicationRepository
    ) {
        this.themeRepository = themeRepository;
        this.champApplicationRepository = champApplicationRepository;
    }

    public boolean isThemeNomUnique(String nom) {
        return !themeRepository.existsByNom(nom);
    }

    public boolean champApplicationExists(String nomChampApplication) {
        return champApplicationRepository.existsByNom(nomChampApplication); // ✅ Vérification FK
    }

    public List<Theme> getThemesByChampApplication(String champApplication) {
        return themeRepository.findByNomChampApplication(champApplication);
    }
}
