package fr.tiogars.data.dev.docs.section.models;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Modèle représentant une section dans l'application.
 * Ce modèle peut être utilisé pour stocker les informations relatives à une
 * section, telles que son
 * nom, sa description et d'autres attributs pertinents.
 */
public class Section {

    /**
     * L'identifiant unique de la section.
     */
    @Schema(description = "L'identifiant unique de la section.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    /**
     * Le nom de la section.
     */
    @Schema(description = "Le nom de la section.", example = "Introduction")
    private String name;

    /**
     * La description de la section.
     */
    @Schema(description = "La description de la section.", example = "Cette section présente les concepts de base.")
    private String description;

    /**
     * L'identifiant du parent direct de la section.
     */
    @Schema(description = "L'identifiant du parent direct de la section.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String parentId;

    /**
     * Les sous-sections rattachées à cette section.
     */
    @Schema(description = "Les sous-sections rattachées à cette section.")
    private List<Section> children = new ArrayList<>();

    public Section() {
    }

    public Section(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Section(String name) {
        this.name = name;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Section> getChildren() {
        return children;
    }

    public void setChildren(List<Section> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Section [name=" + name + ", description=" + description + ", parentId=" + parentId + "]";
    }
}
