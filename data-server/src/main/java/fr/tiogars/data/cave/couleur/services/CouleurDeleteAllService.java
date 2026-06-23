package fr.tiogars.data.cave.couleur.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;

@Service
public class CouleurDeleteAllService {
    private final CouleurRepository couleurRepository;
    public CouleurDeleteAllService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    public void deleteAllCouleurs() { couleurRepository.deleteAllInBatch(); }
}
