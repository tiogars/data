package fr.tiogars.data.system.serverinfo.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class JpaEntityClassInfoListResponse extends GenericListResponse<JpaEntityClassInfo> {

    public JpaEntityClassInfoListResponse(List<JpaEntityClassInfo> items) {
        super(items);
    }
}