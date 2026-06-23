package fr.tiogars.data.cave.vinnom.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.models.VinNomPrintResponse;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomPrintService {
    private final VinNomRepository vinNomRepository;
    private final MaisonRepository maisonRepository;
    public VinNomPrintService(VinNomRepository vinNomRepository, MaisonRepository maisonRepository) { this.vinNomRepository = vinNomRepository; this.maisonRepository = maisonRepository; }
    public VinNomPrintResponse printVinNoms(String mode, String nameFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        List<VinNom> allItems = VinNomModelMapper.toModels(vinNomRepository.findAllByOrderByNameAsc(), maisonRepository);
        String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim();
        List<VinNom> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList();
        return new VinNomPrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size());
    }
}
