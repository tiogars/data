package fr.tiogars.data.dev.docs.brick.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.brick.models.Brick;
import fr.tiogars.data.dev.docs.brick.models.BrickListResponse;
import fr.tiogars.data.dev.docs.brick.repositories.BrickRepository;

@Service
public class BrickListService {

    private final BrickRepository brickRepository;

    public BrickListService(BrickRepository brickRepository) {
        this.brickRepository = brickRepository;
    }

    public BrickListResponse listBricks() {
        List<Brick> items = brickRepository.findAllByOrderByNumberAsc().stream()
            .map(BrickModelMapper::toModel)
            .toList();

        return new BrickListResponse(items, items.size());
    }
}
