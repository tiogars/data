package fr.tiogars.data.dev.docs.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.brick.entities.ExternalLinkEntity;
import fr.tiogars.data.dev.docs.brick.models.ExternalLink;
import fr.tiogars.data.dev.docs.brick.repositories.ExternalLinkRepository;

@Service
public class ExternalLinkUpdateService {

    private final ExternalLinkRepository externalLinkRepository;
    private final ExternalLinkCreationService externalLinkCreationService;

    public ExternalLinkUpdateService(
        ExternalLinkRepository externalLinkRepository,
        ExternalLinkCreationService externalLinkCreationService
    ) {
        this.externalLinkRepository = externalLinkRepository;
        this.externalLinkCreationService = externalLinkCreationService;
    }

    public ExternalLink updateExternalLink(String id, ExternalLink update) {
        ExternalLinkEntity entity = externalLinkRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Lien externe non trouve pour l'id: " + id));

        externalLinkCreationService.validateUniqueName(update.getName(), id);
        ExternalLinkCreationService.applyValues(entity, update.getName(), update.getUrl(), update.isEnabled());

        return BrickModelMapper.toModel(externalLinkRepository.save(entity));
    }
}
