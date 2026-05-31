package fr.tiogars.data.settings.urlmanager.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class ManagedUrl {

    @Schema(description = "Identifiant unique du lien.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Libelle du lien.", example = "Board Sprint")
    private String label;

    @Schema(description = "URL cible.", example = "https://jira.exemple.fr/board/42")
    private String url;

    @Schema(description = "Liste des tags associes.", example = "[\"sprint\",\"team-a\"]")
    private List<String> tags;

    @Schema(description = "Description optionnelle.", example = "Tableau de suivi du sprint en cours")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
