package fr.tiogars.data.dev.docs.gtin.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Gtin {

    @Schema(description = "L'identifiant unique du GTIN.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le code GTIN.", example = "0123456789012")
    private String code;

    @Schema(description = "La description du GTIN.", example = "Produit exemple")
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
