package fr.tiogars.data.cave.maison.models;

import java.util.List;
import fr.tiogars.data.common.models.GenericListResponse;

public class MaisonListResponse extends GenericListResponse<Maison> { public MaisonListResponse(List<Maison> items) { super(items); } public MaisonListResponse(List<Maison> items,int count) { super(items); setCount(count); } }
