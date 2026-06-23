package fr.tiogars.data.cave.vinnom.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class VinNomCreationForm {
    @Schema(description = "Le nom du vin.", example = "Cuvee Reserve")
    private String name;
    @Schema(description = "L'identifiant de la maison associee.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String maisonId;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMaisonId() { return maisonId; }
    public void setMaisonId(String maisonId) { this.maisonId = maisonId; }
}
