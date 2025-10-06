package com.example.pisma.service;

import com.example.pisma.domain.*;
import com.example.pisma.dto.*;
import com.example.pisma.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LetterService {
    private final LetterRepository letterRepo;
    private final DrugRepository drugRepo;
    private final LetterDrugRepository ldRepo;
    private final NotificationService notifier;

    public Page<Letter> publicSearch(String q, Pageable p) {
        if (q == null || q.isBlank())
            return letterRepo.findByActiveTrueAndTitleContainingIgnoreCase("", p);
        return letterRepo.findByActiveTrueAndTitleContainingIgnoreCase(q, p);
    }

    public Page<Letter> adminSearch(String q, Pageable p) {
        if (q == null || q.isBlank())
            return letterRepo.findAll(p);
        return letterRepo.findByTitleContainingIgnoreCase(q, p);
    }

    @Transactional
    public Letter create(LetterCreateDto dto, String pdfPath) {
        Letter l = Letter.builder()
                .title(dto.getTitle())
                .pdfPath(pdfPath)
                .active(true)
                .createdAt(Instant.now())
                .build();
        l = letterRepo.save(l);
        attachDrugs(l, dto.getDrugIds());
        notifier.notifyAll("Novo pismo: " + l.getTitle(), "Objavljeno je novo pismo: " + l.getTitle());
        return l;
    }

    @Transactional
    public Letter update(Long id, LetterUpdateDto dto, String newPdfPathOrNull) {
        Letter l = letterRepo.findById(id).orElseThrow();
        if (dto.getTitle() != null) l.setTitle(dto.getTitle());
        if (dto.getActive() != null) l.setActive(dto.getActive());
        if (newPdfPathOrNull != null) l.setPdfPath(newPdfPathOrNull);
        l.setUpdatedAt(Instant.now());

        // reset relationships
        l.getLetterDrugs().clear();
        letterRepo.save(l);
        attachDrugs(l, dto.getDrugIds());

        notifier.notifyAll("Izmena pisma: " + l.getTitle(), "Pismo je izmenjeno: " + l.getTitle());
        return l;
    }

    @Transactional
    public void deactivate(Long id) {
        Letter l = letterRepo.findById(id).orElseThrow();
        l.setActive(false);
        l.setUpdatedAt(Instant.now());
        letterRepo.save(l);
        notifier.notifyAll("Deaktivacija pisma: " + l.getTitle(), "Pismo je deaktivirano: " + l.getTitle());
    }

    private void attachDrugs(Letter l, List<Long> ids) {
        if (ids == null) return;

        Set<Long> existingDrugIds = l.getLetterDrugs().stream()
                .map(ld -> ld.getDrug().getId())
                .collect(Collectors.toSet());

        for (Long did : ids) {
            if (existingDrugIds.contains(did)) {
                // Skip if the LetterDrug already exists
                continue;
            }

            Drug d = drugRepo.findById(did).orElseThrow();
            LetterDrug ld = LetterDrug.builder().letter(l).drug(d).build();
            l.getLetterDrugs().add(ld); // Add the new association
        }

        // No need to manually save LetterDrug, just save Letter and CascadeType.ALL handles it
        letterRepo.save(l);
    }
}