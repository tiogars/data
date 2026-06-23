package fr.tiogars.data.cave.circonstance.models;

import java.util.List;
import fr.tiogars.data.common.models.GenericListResponse;

public class CirconstanceListResponse extends GenericListResponse<Circonstance> {
    public CirconstanceListResponse(List<Circonstance> items) { super(items); }
    public CirconstanceListResponse(List<Circonstance> items, int count) { super(items); setCount(count); }
}
