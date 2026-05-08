package fr.tiogars.data.dev.docs.brick.models;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class Brick {

    @Schema(description = "Identifiant unique de la brique.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Numero de reference de la brique.", example = "60284")
    private String number;

    @Schema(description = "Titre de la brique.", example = "Le camion de chantier")
    private String title;

    @Schema(description = "Tags de classification de la brique.", example = "[\"city\",\"truck\"]")
    private List<String> tags;

    @Schema(description = "Image en data URL base64.", example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
    private String imageBase64;

    @Schema(description = "Date de creation de la brique.", example = "2025-12-22T12:14:59.569Z")
    private Instant createdAt;

    @Schema(description = "Date de derniere mise a jour de la brique.", example = "2025-12-31T10:43:24.201Z")
    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
