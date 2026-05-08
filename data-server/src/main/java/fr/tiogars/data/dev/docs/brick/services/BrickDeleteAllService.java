package fr.tiogars.data.dev.docs.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.brick.repositories.BrickRepository;

@Service
public class BrickDeleteAllService {

    private final BrickRepository brickRepository;

    public BrickDeleteAllService(BrickRepository brickRepository) {
        this.brickRepository = brickRepository;
    }

    public void deleteAllBricks() {
        brickRepository.deleteAllInBatch();
    }
}
