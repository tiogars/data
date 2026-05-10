package fr.tiogars.data.games.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.games.brick.repositories.ExternalLinkRepository;

@Service
public class ExternalLinkDeleteOneService {

    private final ExternalLinkRepository externalLinkRepository;

    public ExternalLinkDeleteOneService(ExternalLinkRepository externalLinkRepository) {
        this.externalLinkRepository = externalLinkRepository;
    }

    public void deleteExternalLinkById(String id) {
        if (!externalLinkRepository.existsById(id)) {
            throw new DataNotFoundException("Lien externe non trouve pour l'id: " + id);
        }

        externalLinkRepository.deleteById(id);
    }
}
