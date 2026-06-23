package fr.tiogars.data.cave.vinnom.forms;

import java.util.List;

import fr.tiogars.data.cave.vinnom.models.VinNom;
import io.swagger.v3.oas.annotations.media.Schema;

public class VinNomImportForm {
    @Schema(description = "Texte a importer. Chaque ligne non vide represente le nom d'un vin.", example = "Cuvee Reserve")
    private String text;
    @Schema(description = "Format historique JSON: liste des vins a importer.")
    private List<VinNom> items;
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<VinNom> getItems() { return items; }
    public void setItems(List<VinNom> items) { this.items = items; }
}
