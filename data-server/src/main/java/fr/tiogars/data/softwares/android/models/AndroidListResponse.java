package fr.tiogars.data.softwares.android.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class AndroidListResponse extends GenericListResponse<Android> {

    public AndroidListResponse(List<Android> items) {
        super(items);
    }

    public AndroidListResponse(List<Android> items, int count) {
        super(items);
        setCount(count);
    }
}