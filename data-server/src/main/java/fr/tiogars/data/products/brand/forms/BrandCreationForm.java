package fr.tiogars.data.products.brand.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class BrandCreationForm {

    @Schema(description = "Le nom de la marque.", example = "Lego")
    private String name;

    @Schema(description = "La description de la marque.", example = "Produit exemple")
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
