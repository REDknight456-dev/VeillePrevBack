package com.cww.veille_springboot.service;

import com.cww.veille_springboot.entity.Loi;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
@Service

public class LoiService {
    public String generateTitre(Loi loi) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
        String dateFormatted = loi.getDate().format(formatter); // Format : 29 janvier 2025

        return String.format("%s n° %d-%d du %s portant %s",
                loi.getType(), loi.getDate().getYear(), loi.getNumero(), dateFormatted, loi.getNom());
    }
}


