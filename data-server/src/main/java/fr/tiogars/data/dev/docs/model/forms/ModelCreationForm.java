package fr.tiogars.data.dev.docs.model.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class ModelCreationForm {

    @Schema(description = "Le nom du modele.", example = "Modele Catalogue")
    private String name;

    @Schema(description = "La description du modele.", example = "Structure des donnees de reference")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
