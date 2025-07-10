package com.cww.veille_springboot.service;

import com.cww.veille_springboot.entity.TexteReglementaire;
import com.cww.veille_springboot.entity.TextReglementaireVersion;
import com.cww.veille_springboot.repository.ChampApplicationRepository;
import com.cww.veille_springboot.repository.LoiRepository;
import com.cww.veille_springboot.repository.CodeRepository;
import com.cww.veille_springboot.repository.TexteReglementaireRepository;
import com.cww.veille_springboot.repository.ThemeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;


@Service
public class TexteReglementaireService {

    private final LoiRepository loiRepository;
    private final CodeRepository codeRepository;
    private final TexteReglementaireRepository texteReglementaireRepository;
    private final ThemeRepository themeRepository;
    private final ChampApplicationRepository champApplicationRepository;
    private final TextReglementaireVersionService versionService;

    public TexteReglementaireService(
            LoiRepository loiRepository,
            CodeRepository codeRepository,
            TexteReglementaireRepository texteReglementaireRepository,
            ThemeRepository themeRepository,
            ChampApplicationRepository champApplicationRepository,
            TextReglementaireVersionService versionService
    ) {
        this.loiRepository = loiRepository;
        this.codeRepository = codeRepository;
        this.texteReglementaireRepository = texteReglementaireRepository;
        this.themeRepository = themeRepository;
        this.champApplicationRepository = champApplicationRepository;
        this.versionService = versionService;
    }

    public Page<TexteReglementaire> searchTexteReglementaire(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return texteReglementaireRepository.findAll(pageable);
        } else {
            return texteReglementaireRepository.searchAllAttributes(searchTerm.toLowerCase(), pageable);
        }
    }
    public Page<TexteReglementaire> filterAndSearchTexteReglementaire(String champApplication, String theme, String loiTitre, String searchTerm, Pageable pageable) {
        return texteReglementaireRepository.findByChampApplicationAndThemeAndLoiTitreAndSearchTerm(
            champApplication,
            theme,
            loiTitre,
            searchTerm != null ? searchTerm.toLowerCase() : null,
            pageable
        );
    }



    public boolean isLoiTitreExists(String loiTitre) {
        return loiRepository.existsByTitre(loiTitre);
    }

    public boolean isCodeNomExists(String codeNom) {
        return codeRepository.existsByNom(codeNom);
    }

    public boolean isThemeNomExists(String themeNom) {
        return themeRepository.existsByNom(themeNom);
    }

    public boolean isChampApplicationNomExists(String champApplicationNom) {
        return champApplicationRepository.existsByNom(champApplicationNom);
    }



    @Transactional
    public TexteReglementaire createTexteReglementaire(TexteReglementaire texteReglementaire) {
        validateLoiAndCodeAndThemeAndChampApplication(texteReglementaire);
        validatePieceJointe(texteReglementaire);
        texteReglementaire.setVersion(1);
        return texteReglementaireRepository.save(texteReglementaire);
    }

    @Transactional
    public TexteReglementaire updateTexteReglementaire(Integer id, TexteReglementaire texteDetails) {
        TexteReglementaire existingTexte = texteReglementaireRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TexteReglementaire not found with id: " + id));

        validateLoiAndCodeAndThemeAndChampApplication(texteDetails);
        validatePieceJointe(texteDetails);

        existingTexte.setLoiTitre(texteDetails.getLoiTitre());
        existingTexte.setCodeNom(texteDetails.getCodeNom());
        existingTexte.setChampApplication(texteDetails.getChampApplication());
        existingTexte.setTheme(texteDetails.getTheme());
        existingTexte.setTexteResume(texteDetails.getTexteResume());
        existingTexte.setTexte(texteDetails.getTexte());
        existingTexte.setPieceJointe(texteDetails.getPieceJointe());
        existingTexte.setNumeroArticle(texteDetails.getNumeroArticle());
        existingTexte.setVersion(texteDetails.getVersion());

        // Increment the version number
        existingTexte.setVersion(existingTexte.getVersion() != null ? existingTexte.getVersion() + 1 : 1);

        return texteReglementaireRepository.save(existingTexte);
    }

    private void validateLoiAndCodeAndThemeAndChampApplication(TexteReglementaire texteReglementaire) {
        if (!isLoiTitreExists(texteReglementaire.getLoiTitre())) {
            throw new IllegalArgumentException("La loi spécifiée n'existe pas");
        }

        if (!isCodeNomExists(texteReglementaire.getCodeNom())) {
            throw new IllegalArgumentException("Le code spécifié n'existe pas");
        }

        if (texteReglementaire.getTheme() != null && !isThemeNomExists(texteReglementaire.getTheme())) {
            throw new IllegalArgumentException("Le thème spécifié n'existe pas");
        }

        if (!isChampApplicationNomExists(texteReglementaire.getChampApplication())) {
            throw new IllegalArgumentException("Le champ d'application spécifié n'existe pas");
        }
    }

    private void validatePieceJointe(TexteReglementaire texteReglementaire) {
        if (texteReglementaire.getPieceJointe() != null) {
            long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
            if (texteReglementaire.getPieceJointe().length > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("La taille du fichier dépasse 10 Mo");
            }
        }
    }
    public Optional<TexteReglementaire> getTexteReglementaireById(Integer id) {
        return texteReglementaireRepository.findById(id);
    }

    public boolean existsById(Integer id) {
        return texteReglementaireRepository.existsById(id);
    }

    @Transactional
    public void deleteTexteReglementaire(Integer id) {
        // Vérifier s'il existe des versions associées avant de les supprimer
        List<TextReglementaireVersion> versions = versionService.getAllVersionsByTexteId(id);
        if (!versions.isEmpty()) {
            versionService.deleteAllVersionsByTexteId(id);
        }
        // Puis supprimer le texte réglementaire
        texteReglementaireRepository.deleteById(id);
    }
}
