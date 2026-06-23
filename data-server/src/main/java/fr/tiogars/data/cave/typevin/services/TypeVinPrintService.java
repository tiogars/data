package fr.tiogars.data.cave.typevin.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.typevin.models.TypeVin;
import fr.tiogars.data.cave.typevin.models.TypeVinPrintResponse;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;

@Service
public class TypeVinPrintService {
    private final TypeVinRepository typeVinRepository;
    public TypeVinPrintService(TypeVinRepository typeVinRepository) { this.typeVinRepository = typeVinRepository; }
    public TypeVinPrintResponse printTypeVins(String mode, String nameFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        List<TypeVin> allItems = typeVinRepository.findAllByOrderByNameAsc().stream().map(TypeVinModelMapper::toModel).toList();
        String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim();
        List<TypeVin> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList();
        return new TypeVinPrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size());
    }
}
