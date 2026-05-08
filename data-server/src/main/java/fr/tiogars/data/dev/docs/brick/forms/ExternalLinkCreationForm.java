package fr.tiogars.data.dev.docs.brick.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExternalLinkCreationForm {

    @Schema(description = "Nom du lien externe.", example = "BrickLink")
    private String name;

    @Schema(description = "URL de base ou template.", example = "https://www.bricklink.com/v2/search.page?q=")
    private String url;

    @Schema(description = "Indique si le lien est actif.", example = "true")
    private Boolean enabled;

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
