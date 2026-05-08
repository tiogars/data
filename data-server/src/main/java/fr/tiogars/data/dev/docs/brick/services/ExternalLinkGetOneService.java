package fr.tiogars.data.dev.docs.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.brick.models.ExternalLink;
import fr.tiogars.data.dev.docs.brick.repositories.ExternalLinkRepository;

@Service
public class ExternalLinkGetOneService {

    private final ExternalLinkRepository externalLinkRepository;

    public ExternalLinkGetOneService(ExternalLinkRepository externalLinkRepository) {
        this.externalLinkRepository = externalLinkRepository;
    }

    public ExternalLink getExternalLinkById(String id) {
        return externalLinkRepository.findById(id)
            .map(BrickModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Lien externe non trouve pour l'id: " + id));
    }
}
