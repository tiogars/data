package fr.tiogars.data.games.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.games.brick.models.Brick;
import fr.tiogars.data.games.brick.repositories.BrickRepository;

@Service
public class BrickGetOneService {

    private final BrickRepository brickRepository;

    public BrickGetOneService(BrickRepository brickRepository) {
        this.brickRepository = brickRepository;
    }

    public Brick getBrickById(String id) {
        return brickRepository.findById(id)
            .map(BrickModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Brique non trouvee pour l'id: " + id));
    }
}
