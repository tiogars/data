package fr.tiogars.data.dev.docs.model.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Model {

    @Schema(description = "L'identifiant unique du modele.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le nom du modele.", example = "Modele Catalogue")
    private String name;

    @Schema(description = "La description du modele.", example = "Modele utilise pour structurer les donnees produit")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
