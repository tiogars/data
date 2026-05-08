package fr.tiogars.data.dev.docs.brick.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class BrickState {

    @Schema(description = "Collection de briques.")
    private List<Brick> bricks;

    @Schema(description = "Liste globale des tags disponibles.")
    private List<String> tags;

    @Schema(description = "Liste globale des liens externes utilises pour les recherches web.")
    private List<ExternalLink> externalLinks;

    public BrickState() {
    }

    public BrickState(List<Brick> bricks, List<String> tags, List<ExternalLink> externalLinks) {
        this.bricks = bricks;
        this.tags = tags;
        this.externalLinks = externalLinks;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks = bricks;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ExternalLink> getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(List<ExternalLink> externalLinks) {
        this.externalLinks = externalLinks;
    }
}
