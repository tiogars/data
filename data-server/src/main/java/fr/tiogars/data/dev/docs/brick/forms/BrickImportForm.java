package fr.tiogars.data.dev.docs.brick.forms;

import java.util.List;

import fr.tiogars.data.dev.docs.brick.models.Brick;
import fr.tiogars.data.dev.docs.brick.models.ExternalLink;
import io.swagger.v3.oas.annotations.media.Schema;

public class BrickImportForm {

    @Schema(description = "Collection de briques a importer.")
    private List<Brick> bricks;

    @Schema(description = "Liste globale de tags (optionnelle, recalculee lors de l'export).")
    private List<String> tags;

    @Schema(description = "Liste globale de liens externes a importer.")
    private List<ExternalLink> externalLinks;

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
