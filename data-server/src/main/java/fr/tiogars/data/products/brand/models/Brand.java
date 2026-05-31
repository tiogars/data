package fr.tiogars.data.products.brand.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Brand {

    @Schema(description = "L'identifiant unique de la marque.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le nom de la marque.", example = "Lego")
    private String name;

    @Schema(description = "La description de la marque.", example = "Produit exemple")
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
