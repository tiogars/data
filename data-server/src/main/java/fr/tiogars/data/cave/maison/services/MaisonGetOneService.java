package fr.tiogars.data.cave.maison.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonGetOneService { private final MaisonRepository maisonRepository; public MaisonGetOneService(MaisonRepository maisonRepository) { this.maisonRepository = maisonRepository; } public Maison getMaison(String id) { return maisonRepository.findById(id).map(MaisonModelMapper::toModel).orElseThrow(() -> new DataNotFoundException("Maison non trouvee pour l'id: " + id)); } }
