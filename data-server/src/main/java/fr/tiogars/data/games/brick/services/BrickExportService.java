package fr.tiogars.data.games.brick.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import fr.tiogars.data.games.brick.models.Brick;
import fr.tiogars.data.games.brick.models.BrickState;
import fr.tiogars.data.games.brick.models.ExternalLink;
import fr.tiogars.data.games.brick.repositories.BrickRepository;
import fr.tiogars.data.games.brick.repositories.ExternalLinkRepository;

@Service
public class BrickExportService {

    private final BrickRepository brickRepository;
    private final ExternalLinkRepository externalLinkRepository;

    public BrickExportService(BrickRepository brickRepository, ExternalLinkRepository externalLinkRepository) {
        this.brickRepository = brickRepository;
        this.externalLinkRepository = externalLinkRepository;
    }

    public BrickState exportState() {
        List<Brick> bricks = brickRepository.findAllByOrderByNumberAsc().stream()
            .map(BrickModelMapper::toModel)
            .toList();

        List<ExternalLink> externalLinks = externalLinkRepository.findAllByOrderByNameAsc().stream()
            .map(BrickModelMapper::toModel)
            .toList();

        return new BrickState(bricks, collectTags(bricks), externalLinks);
    }

    private static List<String> collectTags(List<Brick> bricks) {
        Set<String> tags = new TreeSet<>();

        for (Brick brick : bricks) {
            tags.addAll(BrickCreationService.normalizeTags(brick.getTags()));
        }

        return new ArrayList<>(tags);
    }
}
