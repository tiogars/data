package fr.tiogars.data.dev.docs.footerlink.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.footerlink.entities.FooterLinkEntity;
import fr.tiogars.data.dev.docs.footerlink.models.FooterLinkListResponse;
import fr.tiogars.data.dev.docs.footerlink.repositories.FooterLinkRepository;

@Service
public class FooterLinkListService {

    private final FooterLinkRepository footerLinkRepository;

    public FooterLinkListService(FooterLinkRepository footerLinkRepository) {
        this.footerLinkRepository = footerLinkRepository;
    }

    public FooterLinkListResponse listFooterLinks() {
        List<FooterLinkEntity> entities = footerLinkRepository.findAllByOrderByDisplayOrderAscLabelAsc();
        return new FooterLinkListResponse(
            entities.stream().map(FooterLinkModelMapper::toFooterLinkModel).toList(),
            entities.size());
    }
}