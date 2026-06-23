package fr.tiogars.data.cave.maison.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.maison.entities.MaisonEntity;
import fr.tiogars.data.cave.maison.forms.MaisonImportForm;
import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.models.MaisonImportResult;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonImportService { private final MaisonRepository maisonRepository; public MaisonImportService(MaisonRepository maisonRepository) { this.maisonRepository = maisonRepository; } private record CandidateMaison(String name, String website) { } @Transactional public MaisonImportResult importMaisons(MaisonImportForm form) { List<CandidateMaison> candidates = buildCandidates(form); if (candidates.isEmpty()) return new MaisonImportResult(List.of(), 0, 0, 0, 0, List.of()); Set<String> existingNames = new HashSet<>(maisonRepository.findAllByOrderByNameAsc().stream().map(MaisonEntity::getName).toList()); Set<String> duplicateNames = new LinkedHashSet<>(); List<Maison> imported = new java.util.ArrayList<>(); int addedCount = 0; int alreadyExistsCount = 0; int invalidCount = 0; for (CandidateMaison candidate : candidates) { try { String normalizedName = requireText(candidate.name(), "Le nom de la maison est obligatoire."); if (existingNames.contains(normalizedName)) { alreadyExistsCount++; duplicateNames.add(normalizedName); continue; } MaisonEntity entity = new MaisonEntity(); MaisonCreationService.applyValues(entity, normalizedName, candidate.website()); var saved = maisonRepository.save(entity); existingNames.add(normalizedName); imported.add(MaisonModelMapper.toModel(saved)); addedCount++; } catch (RuntimeException ex) { invalidCount++; } } return new MaisonImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount, List.copyOf(duplicateNames)); } private List<CandidateMaison> buildCandidates(MaisonImportForm form) { if (form == null) return List.of(); List<CandidateMaison> candidates = new java.util.ArrayList<>(); if (form.getItems() != null) for (Maison item : form.getItems()) if (item != null) candidates.add(new CandidateMaison(item.getName(), item.getWebsite())); if (form.getText() != null && !form.getText().isBlank()) for (String line : form.getText().split("\\R")) { String name = line == null ? null : line.trim(); if (name != null && !name.isEmpty()) candidates.add(new CandidateMaison(name, null)); } return candidates; } }
