package com.cww.veille_springboot.service;

import com.cww.veille_springboot.entity.Code;
import com.cww.veille_springboot.repository.LoiRepository;
import org.springframework.stereotype.Service;

@Service
public class CodeService {
    private final LoiRepository loiRepository;

    public CodeService(LoiRepository loiRepository) {
        this.loiRepository = loiRepository;
    }

    public boolean isLoiNomExists(String nomLoi) {
        // This method checks if the specified nom_loi exists in the loi table
        return loiRepository.findAll().stream()
                .anyMatch(loi -> loi.getNom().equals(nomLoi));
    }
}