package fr.tiogars.data.dev.docs.footerlink.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.footerlink.entities.FooterLinkEntity;
import fr.tiogars.data.dev.docs.footerlink.forms.FooterLinkCreationForm;
import fr.tiogars.data.dev.docs.footerlink.models.FooterLink;
import fr.tiogars.data.dev.docs.footerlink.repositories.FooterLinkRepository;

@Service
public class FooterLinkCreationService {

    private final FooterLinkRepository footerLinkRepository;

    public FooterLinkCreationService(FooterLinkRepository footerLinkRepository) {
        this.footerLinkRepository = footerLinkRepository;
    }

    public FooterLink createFooterLink(FooterLinkCreationForm form) {
        validateUniqueLabel(form.getLabel(), null);

        FooterLinkEntity entity = new FooterLinkEntity();
        applyValues(entity, form.getLabel(), form.getUrl(), form.getIcon(), form.getDisplayOrder());

        return FooterLinkModelMapper.toFooterLinkModel(footerLinkRepository.save(entity));
    }

    static void applyValues(FooterLinkEntity entity, String label, String url, String icon, Integer displayOrder) {
        entity.setLabel(requireText(label, "Le libellé est obligatoire."));
        entity.setUrl(requireText(url, "L'URL est obligatoire."));
        entity.setIcon(requireText(icon, "L'icône est obligatoire."));
        entity.setDisplayOrder(displayOrder != null ? displayOrder : 0);
    }

    void validateUniqueLabel(String label, String currentId) {
        footerLinkRepository.findByLabel(requireText(label, "Le libellé est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Un lien de footer avec ce libellé existe déjà.");
            });
    }

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}