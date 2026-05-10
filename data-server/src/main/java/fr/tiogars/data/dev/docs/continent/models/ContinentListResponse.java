package fr.tiogars.data.dev.docs.continent.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Réponse pour la liste des continents.
 */
@Schema(description = "Liste des continents avec le nombre total.")
public class ContinentListResponse extends GenericListResponse<Continent> {

    public ContinentListResponse(List<Continent> items) {
        super(items);
    }

    public ContinentListResponse(List<Continent> items, int count) {
        super(items);
        setCount(count);
    }
}
