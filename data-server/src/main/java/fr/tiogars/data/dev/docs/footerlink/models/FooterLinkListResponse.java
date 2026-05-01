package fr.tiogars.data.dev.docs.footerlink.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class FooterLinkListResponse extends GenericListResponse<FooterLink> {
    public FooterLinkListResponse(List<FooterLink> items) {
        super(items);
    }

    public FooterLinkListResponse(List<FooterLink> items, int count) {
        super(items);
        setCount(count);
    }
}