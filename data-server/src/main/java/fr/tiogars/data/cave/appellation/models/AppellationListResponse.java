package fr.tiogars.data.cave.appellation.models;

import java.util.List;
import fr.tiogars.data.common.models.GenericListResponse;

public class AppellationListResponse extends GenericListResponse<Appellation> {
    public AppellationListResponse(List<Appellation> items) { super(items); }
    public AppellationListResponse(List<Appellation> items, int count) { super(items); setCount(count); }
}
