package fr.tiogars.data.dev.docs.githubrepository.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubRepositoryCreationForm {

    @Schema(description = "Le propriétaire du repository.", example = "tiogars")
    private String owner;

    @Schema(description = "Le nom du repository.", example = "data")
    private String name;

    @Schema(description = "L'URL du repository.", example = "https://github.com/tiogars/data")
    private String url;

    @Schema(description = "Description fonctionnelle du repository.", example = "Application de gestion de données")
    private String description;

    @Schema(description = "Branche par défaut.", example = "main")
    private String defaultBranch;

    @Schema(description = "Langage principal.", example = "TypeScript")
    private String language;

    @Schema(description = "Nombre d'étoiles.", example = "42")
    private Integer stars;

    @Schema(description = "Indique si le repository est archivé.", example = "false")
    private Boolean archived;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}
