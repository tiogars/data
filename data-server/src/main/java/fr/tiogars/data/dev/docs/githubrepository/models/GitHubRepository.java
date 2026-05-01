package fr.tiogars.data.dev.docs.githubrepository.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class GitHubRepository {

    @Schema(description = "Identifiant unique du repository.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le propriétaire du repository.", example = "tiogars")
    private String owner;

    @Schema(description = "Le nom court du repository.", example = "data")
    private String name;

    @Schema(description = "Le nom complet owner/name du repository.", example = "tiogars/data")
    private String fullName;

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

    @Schema(description = "Indique si le repository existe toujours sur GitHub.", example = "true")
    private Boolean existsOnGitHub;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Boolean getExistsOnGitHub() {
        return existsOnGitHub;
    }

    public void setExistsOnGitHub(Boolean existsOnGitHub) {
        this.existsOnGitHub = existsOnGitHub;
    }
}
