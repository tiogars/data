package fr.tiogars.data.games.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.games.brick.entities.BrickEntity;
import fr.tiogars.data.games.brick.models.Brick;
import fr.tiogars.data.games.brick.repositories.BrickRepository;

@Service
public class BrickUpdateService {

    private final BrickRepository brickRepository;
    private final BrickCreationService brickCreationService;

    public BrickUpdateService(BrickRepository brickRepository, BrickCreationService brickCreationService) {
        this.brickRepository = brickRepository;
        this.brickCreationService = brickCreationService;
    }

    public Brick updateBrick(String id, Brick brickUpdate) {
        BrickEntity entity = brickRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Brique non trouvee pour l'id: " + id));

        brickCreationService.validateUniqueNumber(brickUpdate.getNumber(), id);
        BrickCreationService.applyValues(
            entity,
            brickUpdate.getNumber(),
            brickUpdate.getTitle(),
            brickUpdate.getTags(),
            brickUpdate.getImageBase64()
        );

        return BrickModelMapper.toModel(brickRepository.save(entity));
    }
}
