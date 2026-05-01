package fr.tiogars.data.dev.docs.footerlink.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.footerlink.repositories.FooterLinkRepository;

@Service
public class FooterLinkDeleteAllService {

    private final FooterLinkRepository footerLinkRepository;

    public FooterLinkDeleteAllService(FooterLinkRepository footerLinkRepository) {
        this.footerLinkRepository = footerLinkRepository;
    }

    public void deleteAllFooterLinks() {
        footerLinkRepository.deleteAll();
    }
}