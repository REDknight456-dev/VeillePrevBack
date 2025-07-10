package com.cww.veille_springboot.service;

import com.cww.veille_springboot.DTO.TermeJuridiqueDTO;
import com.cww.veille_springboot.entity.Law;
import com.cww.veille_springboot.entity.TermeJuridique;
import com.cww.veille_springboot.entity.Theme;
import com.cww.veille_springboot.repository.LawRepository;
import com.cww.veille_springboot.repository.TermeJuridiqueRepository;
import com.cww.veille_springboot.repository.ThemeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TermeJuridiqueService {
    private final TermeJuridiqueRepository repository;
    private final LawRepository lawRepository;
    private final ThemeRepository themeRepository;

    public TermeJuridiqueService(TermeJuridiqueRepository repository, LawRepository lawRepository, ThemeRepository themeRepository) {
        this.repository = repository;
        this.lawRepository = lawRepository;
        this.themeRepository = themeRepository;
    }

    public TermeJuridiqueDTO create(TermeJuridiqueDTO dto) {
        if (dto.getTermeDate() == null) {
            throw new IllegalArgumentException("termeDate must not be null");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("name must not be null or empty");
        }
        // Accept date in yyyy-MM-dd format
        dto.setTermeDate(normalizeDate(dto.getTermeDate()));
        Law managedLaw = lawRepository.findById(dto.getLawId())
                .orElseThrow(() -> new IllegalArgumentException("Law not found with id: " + dto.getLawId()));
        Theme managedTheme = themeRepository.findById(dto.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found with id: " + dto.getThemeId()));

        TermeJuridique terme = fromDTO(dto, managedLaw, managedTheme);
        terme.setTitle(generateTitle(terme));
        TermeJuridique saved = repository.save(terme);
        return toDTO(saved);
    }

    public List<TermeJuridiqueDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TermeJuridiqueDTO> findById(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public Optional<TermeJuridiqueDTO> update(Long id, TermeJuridiqueDTO dto) {
        if (dto.getTermeDate() == null) {
            throw new IllegalArgumentException("termeDate must not be null");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("name must not be null or empty");
        }
        dto.setTermeDate(normalizeDate(dto.getTermeDate()));
        return repository.findById(id).map(existing -> {
            Law managedLaw = lawRepository.findById(dto.getLawId())
                    .orElseThrow(() -> new IllegalArgumentException("Law not found with id: " + dto.getLawId()));
            Theme managedTheme = themeRepository.findById(dto.getThemeId())
                    .orElseThrow(() -> new IllegalArgumentException("Theme not found with id: " + dto.getThemeId()));

            existing.setLaw(managedLaw);
            existing.setTheme(managedTheme);
            existing.setSerialNumber(dto.getSerialNumber());
            existing.setType(parseType(dto.getType()));
            existing.setSource(dto.getSource());
            existing.setResume(dto.getResume());
            existing.setFile(dto.getFile());
            existing.setTermeDate(dto.getTermeDate());
            existing.setName(dto.getName());
            existing.setTitle(generateTitle(existing));
            return toDTO(repository.save(existing));
        });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<TermeJuridiqueDTO> findByType(String type) {
        TermeJuridique.Type enumType = parseType(type);
        return repository.findByType(enumType).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Filtering logic with more filters
    public List<TermeJuridiqueDTO> filter(
            String type,
            Integer lawId,
            Integer themeId,
            String startDate,
            String endDate,
            Integer serialNumber,
            String source,
            String resume,
            String title
    ) {
        List<TermeJuridique> termes = repository.findAll();
        Stream<TermeJuridique> stream = termes.stream();

        if (type != null) {
            try {
                TermeJuridique.Type enumType = parseType(type);
                stream = stream.filter(t -> t.getType() == enumType);
            } catch (Exception ignored) {}
        }
        if (lawId != null) {
            stream = stream.filter(t -> t.getLaw() != null && t.getLaw().getIdLaw() == lawId);
        }
        if (themeId != null) {
            stream = stream.filter(t -> t.getTheme() != null && t.getTheme().getId_theme() == themeId);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate != null) {
            try {
                Date start = sdf.parse(startDate);
                stream = stream.filter(t -> t.getTermeDate() != null && !t.getTermeDate().before(start));
            } catch (ParseException ignored) {}
        }
        if (endDate != null) {
            try {
                Date end = sdf.parse(endDate);
                stream = stream.filter(t -> t.getTermeDate() != null && !t.getTermeDate().after(end));
            } catch (ParseException ignored) {}
        }
        if (serialNumber != null) {
            stream = stream.filter(t -> t.getSerialNumber() == serialNumber);
        }
        if (source != null) {
            stream = stream.filter(t -> t.getSource() != null && t.getSource().toLowerCase().contains(source.toLowerCase()));
        }
        if (resume != null) {
            stream = stream.filter(t -> t.getResume() != null && t.getResume().toLowerCase().contains(resume.toLowerCase()));
        }
        if (title != null) {
            stream = stream.filter(t -> t.getTitle() != null && t.getTitle().toLowerCase().contains(title.toLowerCase()));
        }
        return stream.map(this::toDTO).collect(Collectors.toList());
    }

    // Improved search logic: search in all attributes, returns all results (no pagination)
    public List<TermeJuridiqueDTO> searchAll(String q) {
        String query = q.toLowerCase();
        return repository.findAll().stream()
                .filter(t ->
                        (t.getTitle() != null && t.getTitle().toLowerCase().contains(query)) ||
                        (t.getResume() != null && t.getResume().toLowerCase().contains(query)) ||
                        (t.getSource() != null && t.getSource().toLowerCase().contains(query)) ||
                        (t.getName() != null && t.getName().toLowerCase().contains(query)) ||
                        (t.getType() != null && t.getType().name().toLowerCase().contains(query)) ||
                        (t.getLaw() != null && String.valueOf(t.getLaw().getIdLaw()).contains(query)) ||
                        (t.getTheme() != null && String.valueOf(t.getTheme().getId_theme()).contains(query)) ||
                        String.valueOf(t.getSerialNumber()).contains(query) ||
                        (t.getTermeDate() != null && new SimpleDateFormat("yyyy-MM-dd").format(t.getTermeDate()).contains(query))
                )
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Search, filter, and paginate based on all attributes, id_law, id_theme
    public Page<TermeJuridiqueDTO> searchFilterPaginate(String q, Integer lawId, Integer themeId, Pageable pageable) {
        String query = q == null ? "" : q.toLowerCase();
        List<TermeJuridiqueDTO> matched = repository.findAll().stream()
                .filter(t ->
                    (lawId == null || (t.getLaw() != null && t.getLaw().getIdLaw() == lawId)) &&
                    (themeId == null || (t.getTheme() != null && t.getTheme().getId_theme() == themeId)) &&
                    (
                        (t.getTitle() != null && t.getTitle().toLowerCase().contains(query)) ||
                        (t.getResume() != null && t.getResume().toLowerCase().contains(query)) ||
                        (t.getSource() != null && t.getSource().toLowerCase().contains(query)) ||
                        (t.getName() != null && t.getName().toLowerCase().contains(query)) ||
                        (t.getType() != null && t.getType().name().toLowerCase().contains(query)) ||
                        (t.getLaw() != null && String.valueOf(t.getLaw().getIdLaw()).contains(query)) ||
                        (t.getTheme() != null && String.valueOf(t.getTheme().getId_theme()).contains(query)) ||
                        String.valueOf(t.getSerialNumber()).contains(query) ||
                        (t.getTermeDate() != null && new SimpleDateFormat("yyyy-MM-dd").format(t.getTermeDate()).contains(query))
                    )
                )
                .map(this::toDTO)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), matched.size());
        List<TermeJuridiqueDTO> pageContent = matched.subList(start, end);
        return new PageImpl<>(pageContent, pageable, matched.size());
    }

    private List<TermeJuridiqueDTO> filter(String type, Integer lawId, Integer themeId, Object o, Object o1) {
        return null;
    }

    private String generateTitle(TermeJuridique terme) {
        Date date = terme.getTermeDate();
        String year = "";
        String formattedDay = "";
        String monthName = "";
        if (date != null) {
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            year = yearFormat.format(date);
            formattedDay = dayFormat.format(date);

            // French month names
            String[] frenchMonths = {
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
            };
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            int monthIdx = Integer.parseInt(monthFormat.format(date)) - 1;
            monthName = frenchMonths[monthIdx];
        }
        // Example: Code n° 2025-123 du 06 Janvier 2025 portant name
        return String.format("%s n° %s-%d du %s %s %s portant %s",
                terme.getType().name(),
                year,
                terme.getSerialNumber(),
                formattedDay,
                monthName,
                year,
                terme.getName()
        );
    }

    // Helper to normalize date to yyyy-MM-dd (no time)
    private Date normalizeDate(Date input) {
        if (input == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = sdf.format(input);
            return sdf.parse(formatted);
        } catch (Exception e) {
            throw new IllegalArgumentException("termeDate must be in yyyy-MM-dd format");
        }
    }

    // Mapping methods
    private TermeJuridiqueDTO toDTO(TermeJuridique terme) {
        TermeJuridiqueDTO dto = new TermeJuridiqueDTO();
        dto.setId(terme.getId());
        dto.setLawId(terme.getLaw() != null ? terme.getLaw().getIdLaw() : null);
        dto.setThemeId(terme.getTheme() != null ? terme.getTheme().getId_theme() : null);
        dto.setSerialNumber(terme.getSerialNumber());
        dto.setType(terme.getType() != null ? terme.getType().name() : null);
        dto.setSource(terme.getSource());
        dto.setResume(terme.getResume());
        dto.setTitle(terme.getTitle());
        dto.setFile(terme.getFile());
        dto.setTermeDate(terme.getTermeDate());
        dto.setName(terme.getName());
        return dto;
    }

    private TermeJuridique fromDTO(TermeJuridiqueDTO dto, Law law, Theme theme) {
        TermeJuridique terme = new TermeJuridique();
        terme.setLaw(law);
        terme.setTheme(theme);
        terme.setSerialNumber(dto.getSerialNumber());
        terme.setType(parseType(dto.getType()));
        terme.setSource(dto.getSource());
        terme.setResume(dto.getResume());
        terme.setFile(dto.getFile());
        terme.setTermeDate(dto.getTermeDate());
        terme.setName(dto.getName());
        // title is set in create/update
        return terme;
    }

    // Helper to parse type accepting any case (e.g. "CODE", "Code", "code", etc.)
    private TermeJuridique.Type parseType(String type) {
        if (type == null) throw new IllegalArgumentException("Type must not be null");
        try {
            return TermeJuridique.Type.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type: " + type + ". Allowed values: CODE, DECRET, ARRETE, CIRCULAIRE");
        }
    }
}
