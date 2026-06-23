package fr.tiogars.data.cave.maison.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class MaisonCreationForm { @Schema(description = "Le nom de la maison.", example = "Moet & Chandon") private String name; @Schema(description = "Le site web de la maison.", example = "https://example.com") private String website; 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
 }
