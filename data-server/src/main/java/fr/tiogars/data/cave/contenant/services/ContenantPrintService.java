package fr.tiogars.data.cave.contenant.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.models.ContenantPrintResponse;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantPrintService { private final ContenantRepository contenantRepository; public ContenantPrintService(ContenantRepository contenantRepository) { this.contenantRepository = contenantRepository; } public ContenantPrintResponse printContenants(String mode, String nameFilter) { String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase(); if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'."); List<Contenant> allItems = contenantRepository.findAllByOrderByNameAsc().stream().map(ContenantModelMapper::toModel).toList(); String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim(); List<Contenant> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList(); return new ContenantPrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size()); } }
