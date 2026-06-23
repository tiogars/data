package fr.tiogars.data.cave.vinnom.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class VinNom {

    @Schema(description = "L'identifiant unique.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le nom du vin.", example = "Cuvee Reserve")
    private String name;

    @Schema(description = "L'identifiant de la maison associee.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String maisonId;

    @Schema(description = "Le nom de la maison associee.", example = "Moet & Chandon")
    private String maisonName;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMaisonId() { return maisonId; }
    public void setMaisonId(String maisonId) { this.maisonId = maisonId; }
    public String getMaisonName() { return maisonName; }
    public void setMaisonName(String maisonName) { this.maisonName = maisonName; }
}
