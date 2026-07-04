package fr.tiogars.data.settings.sectiondocs.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class SectionDocsSetting {

    @Schema(description = "Identifiant unique du paramétrage de section.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Identifiant de la section racine concernée.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String sectionId;

    @Schema(description = "Chemin relatif sous volumes/docs pour cette section racine.", example = "guides/produits")
    private String storagePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}