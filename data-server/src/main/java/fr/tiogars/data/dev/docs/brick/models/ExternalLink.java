package fr.tiogars.data.dev.docs.brick.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExternalLink {

    @Schema(description = "Identifiant unique du lien externe.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Nom du lien externe.", example = "BrickLink")
    private String name;

    @Schema(description = "URL de base ou template du lien externe.", example = "https://www.bricklink.com/v2/search.page?q=")
    private String url;

    @Schema(description = "Indique si le lien est actif.", example = "true")
    private boolean enabled;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
