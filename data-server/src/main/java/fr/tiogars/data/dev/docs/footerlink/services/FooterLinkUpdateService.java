package fr.tiogars.data.dev.docs.footerlink.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.footerlink.entities.FooterLinkEntity;
import fr.tiogars.data.dev.docs.footerlink.models.FooterLink;
import fr.tiogars.data.dev.docs.footerlink.repositories.FooterLinkRepository;

@Service
public class FooterLinkUpdateService {

    private final FooterLinkRepository footerLinkRepository;
    private final FooterLinkCreationService footerLinkCreationService;

    public FooterLinkUpdateService(
        FooterLinkRepository footerLinkRepository,
        FooterLinkCreationService footerLinkCreationService
    ) {
        this.footerLinkRepository = footerLinkRepository;
        this.footerLinkCreationService = footerLinkCreationService;
    }

    public FooterLink updateFooterLink(String id, FooterLink footerLinkUpdate) {
        FooterLinkEntity entity = footerLinkRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Lien de footer non trouvé pour l'id: " + id));

        footerLinkCreationService.validateUniqueLabel(footerLinkUpdate.getLabel(), id);
        FooterLinkCreationService.applyValues(
            entity,
            footerLinkUpdate.getLabel(),
            footerLinkUpdate.getUrl(),
            footerLinkUpdate.getIcon(),
            footerLinkUpdate.getDisplayOrder());

        return FooterLinkModelMapper.toFooterLinkModel(footerLinkRepository.save(entity));
    }
}