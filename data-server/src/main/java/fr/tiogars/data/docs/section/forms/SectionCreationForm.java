package fr.tiogars.data.docs.section.forms;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Formulaire de création de section.
 * Ce formulaire est utilisé pour recevoir les données nécessaires à la création d'une nouvelle section.
 */
@Schema(description = "Formulaire de création de section.")
public class SectionCreationForm {

    /**
     * Le nom de la section.
     */
    @Schema(description = "Le nom de la section.", example = "Introduction")
    private String name;

    /**
     * La description de la section.
     */
    @Schema(description = "La description de la section.", example = "Cette section introduit le sujet.")
    private String description;

    /**
     * L'identifiant de la section parente.
     */
    @Schema(description = "L'identifiant de la section parente.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String parentId;

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
}
