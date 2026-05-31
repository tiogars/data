package fr.tiogars.data.locations.continent.forms;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Formulaire pour la mise à jour d'un continent.
 */
public class ContinentUpdateForm {

    @Schema(description = "L'identifiant unique du continent.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le code du continent.", example = "eu")
    private String code;

    @Schema(description = "Le nom du continent.", example = "Europe")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
