package fr.tiogars.data.dev.docs.section.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class SectionListResponse extends GenericListResponse<Section> {
    public SectionListResponse(List<Section> items) {
        super(items);
    }
}