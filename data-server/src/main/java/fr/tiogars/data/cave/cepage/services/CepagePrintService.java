package fr.tiogars.data.cave.cepage.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.cepage.models.Cepage;
import fr.tiogars.data.cave.cepage.models.CepagePrintResponse;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;

@Service
public class CepagePrintService {
    private final CepageRepository cepageRepository;
    public CepagePrintService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    public CepagePrintResponse printCepages(String mode, String nameFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        List<Cepage> allItems = cepageRepository.findAllByOrderByNameAsc().stream().map(CepageModelMapper::toModel).toList();
        String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim();
        List<Cepage> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList();
        return new CepagePrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size());
    }
}
