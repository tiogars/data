package fr.tiogars.data.dev.docs.githubrepository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "github_repository")
public class GitHubRepositoryEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "description")
    private String description;

    @Column(name = "default_branch", nullable = false)
    private String defaultBranch;

    @Column(name = "language")
    private String language;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "archived", nullable = false)
    private Boolean archived;

    @Column(name = "exists_on_github")
    private Boolean existsOnGitHub = Boolean.TRUE;

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
