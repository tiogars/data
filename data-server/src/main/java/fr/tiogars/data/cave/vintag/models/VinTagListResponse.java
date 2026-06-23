package fr.tiogars.data.cave.vintag.models;

import java.util.List;
import fr.tiogars.data.common.models.GenericListResponse;

public class VinTagListResponse extends GenericListResponse<VinTag> {
    public VinTagListResponse(List<VinTag> items) { super(items); }
    public VinTagListResponse(List<VinTag> items, int count) { super(items); setCount(count); }
}
