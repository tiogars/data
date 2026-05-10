package fr.tiogars.data.dev.docs.continent.forms;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Formulaire pour la création d'un continent.
 */
public class ContinentCreationForm {

    @Schema(description = "Le code du continent.", example = "eu")
    private String code;

    @Schema(description = "Le nom du continent.", example = "Europe")
    private String name;

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
