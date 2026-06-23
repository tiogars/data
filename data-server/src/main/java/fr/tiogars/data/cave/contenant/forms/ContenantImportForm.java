package fr.tiogars.data.cave.contenant.forms;

import java.util.List;
import fr.tiogars.data.cave.contenant.models.Contenant;
import io.swagger.v3.oas.annotations.media.Schema;

public class ContenantImportForm { @Schema(description = "Texte a importer. Chaque ligne non vide represente un nom.", example = "Bouteille\nExemple") private String text; @Schema(description = "Format historique JSON: liste des contenants a importer.") private List<Contenant> items; 
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<Contenant> getItems() { return items; }
    public void setItems(List<Contenant> items) { this.items = items; }
 }
