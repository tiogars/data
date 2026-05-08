package fr.tiogars.data.dev.docs.brick.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.brick.entities.ExternalLinkEntity;
import fr.tiogars.data.dev.docs.brick.forms.ExternalLinkCreationForm;
import fr.tiogars.data.dev.docs.brick.models.ExternalLink;
import fr.tiogars.data.dev.docs.brick.repositories.ExternalLinkRepository;

@Service
public class ExternalLinkCreationService {

    private final ExternalLinkRepository externalLinkRepository;

    public ExternalLinkCreationService(ExternalLinkRepository externalLinkRepository) {
        this.externalLinkRepository = externalLinkRepository;
    }

    public ExternalLink createExternalLink(ExternalLinkCreationForm form) {
        validateUniqueName(form.getName(), null);

        ExternalLinkEntity entity = new ExternalLinkEntity();
        applyValues(entity, form.getName(), form.getUrl(), form.getEnabled());

        return BrickModelMapper.toModel(externalLinkRepository.save(entity));
    }

    static void applyValues(ExternalLinkEntity entity, String name, String url, Boolean enabled) {
        entity.setName(requireText(name, "Le nom du lien externe est obligatoire."));
        entity.setUrl(requireText(url, "L'URL du lien externe est obligatoire."));
        entity.setEnabled(enabled == null || enabled);
    }

    void validateUniqueName(String name, String currentId) {
        externalLinkRepository.findByNameIgnoreCase(requireText(name, "Le nom du lien externe est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Un lien externe avec ce nom existe deja.");
            });
    }

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
