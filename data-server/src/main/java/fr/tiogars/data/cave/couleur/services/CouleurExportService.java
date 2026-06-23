package fr.tiogars.data.cave.couleur.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;
import fr.tiogars.data.cave.couleur.models.CouleurListResponse;

@Service
public class CouleurExportService {
    private final CouleurRepository couleurRepository;
    public CouleurExportService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    public CouleurListResponse exportCouleurs() { var items = couleurRepository.findAllByOrderByNameAsc().stream().map(CouleurModelMapper::toModel).toList(); return new CouleurListResponse(items, items.size()); }
}
