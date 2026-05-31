package fr.tiogars.data.settings.footerlink.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.settings.footerlink.models.FooterLink;
import fr.tiogars.data.settings.footerlink.repositories.FooterLinkRepository;

@Service
public class FooterLinkGetOneService {

    private final FooterLinkRepository footerLinkRepository;

    public FooterLinkGetOneService(FooterLinkRepository footerLinkRepository) {
        this.footerLinkRepository = footerLinkRepository;
    }

    public FooterLink getFooterLinkById(String id) {
        return footerLinkRepository.findById(id)
            .map(FooterLinkModelMapper::toFooterLinkModel)
            .orElseThrow(() -> new DataNotFoundException("Lien de footer non trouvé pour l'id: " + id));
    }
}