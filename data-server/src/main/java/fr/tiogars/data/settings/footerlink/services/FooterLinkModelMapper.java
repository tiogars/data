package fr.tiogars.data.settings.footerlink.services;

import fr.tiogars.data.settings.footerlink.entities.FooterLinkEntity;
import fr.tiogars.data.settings.footerlink.models.FooterLink;

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