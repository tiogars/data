package fr.tiogars.data.cave.couleur.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;
import fr.tiogars.data.cave.couleur.models.CouleurListResponse;

@Service
public class CouleurListService {
    private final CouleurRepository couleurRepository;
    public CouleurListService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    public CouleurListResponse listCouleurs() { var entities = couleurRepository.findAllByOrderByNameAsc(); return new CouleurListResponse(entities.stream().map(CouleurModelMapper::toModel).toList(), entities.size()); }
}
