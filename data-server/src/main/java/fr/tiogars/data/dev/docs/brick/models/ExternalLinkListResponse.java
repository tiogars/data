package fr.tiogars.data.dev.docs.brick.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class ExternalLinkListResponse extends GenericListResponse<ExternalLink> {
    public ExternalLinkListResponse(List<ExternalLink> items) {
        super(items);
    }

    public ExternalLinkListResponse(List<ExternalLink> items, int count) {
        super(items);
        setCount(count);
    }
}
