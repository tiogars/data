package fr.tiogars.data.docs.sectiondocument.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class SectionDocument {

    @Schema(description = "Identifiant du document.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Nom du document.", example = "Guide produit")
    private String name;

    @Schema(description = "Chemin relatif du document sous volumes/docs.", example = "guides/produits")
    private String storagePath;

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

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}
