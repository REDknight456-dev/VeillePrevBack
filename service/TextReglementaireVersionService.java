package com.cww.veille_springboot.service;

import com.cww.veille_springboot.entity.TexteReglementaire;
import com.cww.veille_springboot.entity.TextReglementaireVersion;
import com.cww.veille_springboot.repository.TextReglementaireVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TextReglementaireVersionService {
    private final TextReglementaireVersionRepository versionRepository;

    public TextReglementaireVersionService(TextReglementaireVersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Transactional
    public TextReglementaireVersion createVersion(TexteReglementaire texteReglementaire) {
        // Create new version object
        TextReglementaireVersion version = new TextReglementaireVersion();

        // Copy all fields from the TexteReglementaire
        version.setIdTexteReglementaire(texteReglementaire.getIdTexteReglementaire());
        version.setLoiTitre(texteReglementaire.getLoiTitre());
        version.setTheme(texteReglementaire.getTheme());
        version.setCodeNom(texteReglementaire.getCodeNom());
        version.setChampApplication(texteReglementaire.getChampApplication());
        version.setTexteResume(texteReglementaire.getTexteResume());
        version.setTexte(texteReglementaire.getTexte());
        version.setPieceJointe(texteReglementaire.getPieceJointe());
        version.setNumeroArticle(texteReglementaire.getNumeroArticle());
        version.setVersion(texteReglementaire.getVersion());

        // Save and return the new version
        return versionRepository.save(version);
    }

    public List<TextReglementaireVersion> getAllVersionsByTexteId(Integer texteReglementaireId) {
        return versionRepository.findByIdTexteReglementaire(texteReglementaireId);
    }

    @Transactional
    public void deleteAllVersionsByTexteId(Integer texteId) {
        versionRepository.deleteAllByTexteReglementaireId(texteId);
    }


}
