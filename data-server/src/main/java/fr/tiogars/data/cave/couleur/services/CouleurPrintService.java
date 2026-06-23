package fr.tiogars.data.cave.couleur.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.models.CouleurPrintResponse;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;

@Service
public class CouleurPrintService {
    private final CouleurRepository couleurRepository;
    public CouleurPrintService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    public CouleurPrintResponse printCouleurs(String mode, String nameFilter) {
        String effectiveMode = mode == null || mode.isBlank() ? "filtered" : mode.trim().toLowerCase();
        if (!"filtered".equals(effectiveMode) && !"all".equals(effectiveMode)) throw new IllegalArgumentException("Le mode d'impression doit etre 'filtered' ou 'all'.");
        List<Couleur> allItems = couleurRepository.findAllByOrderByNameAsc().stream().map(CouleurModelMapper::toModel).toList();
        String filter = nameFilter == null || nameFilter.isBlank() ? null : nameFilter.trim();
        List<Couleur> filtered = "all".equals(effectiveMode) ? allItems : allItems.stream().filter(item -> filter == null || (item.getName() != null && item.getName().toLowerCase().contains(filter.toLowerCase()))).toList();
        return new CouleurPrintResponse(filtered, filtered.size(), OffsetDateTime.now().toString(), allItems.size());
    }
}
