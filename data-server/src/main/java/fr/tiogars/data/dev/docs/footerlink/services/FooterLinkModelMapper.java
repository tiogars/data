package fr.tiogars.data.dev.docs.footerlink.services;

import fr.tiogars.data.dev.docs.footerlink.entities.FooterLinkEntity;
import fr.tiogars.data.dev.docs.footerlink.models.FooterLink;

final class FooterLinkModelMapper {

    private FooterLinkModelMapper() {
    }

    static FooterLink toFooterLinkModel(FooterLinkEntity entity) {
        FooterLink model = new FooterLink();
        model.setId(entity.getId());
        model.setLabel(entity.getLabel());
        model.setUrl(entity.getUrl());
        model.setIcon(entity.getIcon());
        model.setDisplayOrder(entity.getDisplayOrder());
        return model;
    }
}