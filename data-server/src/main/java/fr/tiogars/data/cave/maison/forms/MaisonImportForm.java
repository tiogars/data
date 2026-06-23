package fr.tiogars.data.cave.maison.forms;

import java.util.List;
import fr.tiogars.data.cave.maison.models.Maison;
import io.swagger.v3.oas.annotations.media.Schema;

public class MaisonImportForm { @Schema(description = "Texte a importer. Chaque ligne non vide represente un nom.", example = "Moet & Chandon\nExemple") private String text; @Schema(description = "Format historique JSON: liste des maisons a importer.") private List<Maison> items; 
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<Maison> getItems() { return items; }
    public void setItems(List<Maison> items) { this.items = items; }
 }
