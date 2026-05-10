package fr.tiogars.data.games.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.games.brick.repositories.BrickRepository;

@Service
public class BrickDeleteOneService {

    private final BrickRepository brickRepository;

    public BrickDeleteOneService(BrickRepository brickRepository) {
        this.brickRepository = brickRepository;
    }

    public void deleteBrickById(String id) {
        if (!brickRepository.existsById(id)) {
            throw new DataNotFoundException("Brique non trouvee pour l'id: " + id);
        }

        brickRepository.deleteById(id);
    }
}
