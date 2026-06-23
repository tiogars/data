package fr.tiogars.data.cave.vinnom.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

public class VinNomListResponse extends GenericListResponse<VinNom> {
    public VinNomListResponse(List<VinNom> items) { super(items); }
    public VinNomListResponse(List<VinNom> items, int count) { super(items); setCount(count); }
}
