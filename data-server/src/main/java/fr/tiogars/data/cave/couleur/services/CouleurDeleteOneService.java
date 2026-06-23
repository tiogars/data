package fr.tiogars.data.cave.couleur.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;

@Service
public class CouleurDeleteOneService {
    private final CouleurRepository couleurRepository;
    public CouleurDeleteOneService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    public void deleteCouleur(String id) { if (!couleurRepository.existsById(id)) throw new fr.tiogars.data.common.exceptions.DataNotFoundException("Couleur non trouvee pour l'id: " + id); couleurRepository.deleteById(id); }
}
