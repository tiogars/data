package fr.tiogars.data.dev.docs.urlmanager.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class UrlCardConfig {

    @Schema(description = "Identifiant unique de la carte.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Titre de la carte affichee sur l'accueil.", example = "Equipe Dev")
    private String title;

    @Schema(description = "Tags utilises pour filtrer les liens.", example = "[\"dev\",\"backend\"]")
    private List<String> tags;

    @Schema(description = "Mode de filtre: any (au moins un tag) ou all (tous les tags).", example = "all")
    private String matchMode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(String matchMode) {
        this.matchMode = matchMode;
    }
}
