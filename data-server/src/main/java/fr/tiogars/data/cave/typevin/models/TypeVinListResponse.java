package fr.tiogars.data.cave.typevin.models;

import java.util.List;
import fr.tiogars.data.common.models.GenericListResponse;

public class TypeVinListResponse extends GenericListResponse<TypeVin> {
    public TypeVinListResponse(List<TypeVin> items) { super(items); }
    public TypeVinListResponse(List<TypeVin> items, int count) { super(items); setCount(count); }
}
