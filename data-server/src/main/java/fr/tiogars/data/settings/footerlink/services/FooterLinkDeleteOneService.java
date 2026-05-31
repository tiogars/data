package fr.tiogars.data.settings.footerlink.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.settings.footerlink.repositories.FooterLinkRepository;

@Service
public class FooterLinkDeleteOneService {

    private final FooterLinkRepository footerLinkRepository;

    public FooterLinkDeleteOneService(FooterLinkRepository footerLinkRepository) {
        this.footerLinkRepository = footerLinkRepository;
    }

    public void deleteFooterLinkById(String id) {
        if (!footerLinkRepository.existsById(id)) {
            throw new DataNotFoundException("Lien de footer non trouvé pour l'id: " + id);
        }

        footerLinkRepository.deleteById(id);
    }
}