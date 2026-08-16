package fr.tiogars.data.system.serverinfo.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class DomainPathListResponse extends GenericListResponse<String> {

    public DomainPathListResponse(List<String> items) {
        super(items);
    }
}
