package fr.tiogars.data.games.brick.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.games.brick.models.ExternalLink;
import fr.tiogars.data.games.brick.models.ExternalLinkListResponse;
import fr.tiogars.data.games.brick.repositories.ExternalLinkRepository;

@Service
public class ExternalLinkListService {

    private final ExternalLinkRepository externalLinkRepository;

    public ExternalLinkListService(ExternalLinkRepository externalLinkRepository) {
        this.externalLinkRepository = externalLinkRepository;
    }

    public ExternalLinkListResponse listExternalLinks() {
        List<ExternalLink> items = externalLinkRepository.findAllByOrderByNameAsc().stream()
            .map(BrickModelMapper::toModel)
            .toList();

        return new ExternalLinkListResponse(items, items.size());
    }
}
