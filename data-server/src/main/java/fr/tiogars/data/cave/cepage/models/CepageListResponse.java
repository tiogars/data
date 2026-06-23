package fr.tiogars.data.cave.cepage.models;

import java.util.List;
import fr.tiogars.data.common.models.GenericListResponse;

public class CepageListResponse extends GenericListResponse<Cepage> {
    public CepageListResponse(List<Cepage> items) { super(items); }
    public CepageListResponse(List<Cepage> items, int count) { super(items); setCount(count); }
}
