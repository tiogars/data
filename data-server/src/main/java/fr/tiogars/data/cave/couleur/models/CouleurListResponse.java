package fr.tiogars.data.cave.couleur.models;

import java.util.List;
import fr.tiogars.data.common.models.GenericListResponse;

public class CouleurListResponse extends GenericListResponse<Couleur> {
    public CouleurListResponse(List<Couleur> items) { super(items); }
    public CouleurListResponse(List<Couleur> items, int count) { super(items); setCount(count); }
}
