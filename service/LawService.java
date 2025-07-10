package com.cww.veille_springboot.service;

import com.cww.veille_springboot.DTO.LawDTO;
import com.cww.veille_springboot.entity.Law;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class LawService {

    public String generateTitre(Law law) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH);
        String formattedDate = law.getDate().format(formatter);
        String numeroFormatted = String.format("%02d", law.getNumero());
        return String.format("Loi n° %d-%s du %s portant %s",
                law.getDate().getYear(), numeroFormatted, formattedDate, law.getNom());
    }

    public LawDTO toDTO(Law law) {
        LawDTO dto = new LawDTO();
        dto.setIdLaw(law.getIdLaw());
        dto.setIdDomaine(law.getIdDomaine());
        dto.setIdSousDomaine(law.getIdSousDomaine());
        dto.setNumero(law.getNumero());
        dto.setDate(law.getDate());
        dto.setNom(law.getNom());
        dto.setSource(law.getSource());
        dto.setResume(law.getResume());
        dto.setTitre(law.getTitre());
        if (law.getPJointe() != null && law.getPJointe().length > 0) {
            dto.setPJointe(Base64.getEncoder().encodeToString(law.getPJointe()));
        }
        return dto;
    }

    public Law fromDTO(LawDTO dto) {
        Law law = new Law();
        law.setIdLaw(dto.getIdLaw());
        law.setIdDomaine(dto.getIdDomaine());
        law.setIdSousDomaine(dto.getIdSousDomaine());
        law.setNumero(dto.getNumero());
        law.setDate(dto.getDate());
        law.setNom(dto.getNom());
        law.setSource(dto.getSource());
        law.setResume(dto.getResume());
        law.setTitre(dto.getTitre());

        if (dto.getPJointe() != null && !dto.getPJointe().isEmpty()) {
            law.setPJointe(Base64.getDecoder().decode(dto.getPJointe()));
        } else {
            law.setPJointe(new byte[0]);
        }

        return law;
    }

    public List<LawDTO> toDTOList(List<Law> laws) {
        return laws.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
