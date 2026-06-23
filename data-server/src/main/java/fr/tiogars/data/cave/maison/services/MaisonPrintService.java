package fr.tiogars.data.cave.maison.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.models.MaisonPrintResponse;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonPrintService { private final MaisonRepository maisonRepository; public MaisonPrintService(MaisonRepository maisonRepository) { this.maisonRepository = maisonRepository; } public MaisonPrintResponse printMaisons(String mode, String nameFilter) { String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase(); if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'."); List<Maison> allItems = maisonRepository.findAllByOrderByNameAsc().stream().map(MaisonModelMapper::toModel).toList(); String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim(); List<Maison> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList(); return new MaisonPrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size()); } }
