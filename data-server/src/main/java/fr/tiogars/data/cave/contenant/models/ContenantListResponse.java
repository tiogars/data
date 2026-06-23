package fr.tiogars.data.cave.contenant.models;

import java.util.List;
import fr.tiogars.data.common.models.GenericListResponse;

public class ContenantListResponse extends GenericListResponse<Contenant> { public ContenantListResponse(List<Contenant> items) { super(items); } public ContenantListResponse(List<Contenant> items,int count) { super(items); setCount(count); } }
