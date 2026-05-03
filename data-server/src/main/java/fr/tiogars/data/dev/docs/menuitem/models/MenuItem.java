package fr.tiogars.data.dev.docs.menuitem.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class MenuItem {

    @Schema(description = "L'identifiant unique de l'entree de menu.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le libelle affiche dans le menu.", example = "Sections")
    private String label;

    @Schema(description = "Le chemin de navigation React Router.", example = "/section")
    private String path;

    @Schema(description = "La cle d'icone MUI utilisee par l'application web.", example = "inbox")
    private String icon;

    @Schema(description = "L'ordre d'affichage de l'entree dans le menu.", example = "10")
    private Integer displayOrder;

    @Schema(description = "Indique si l'entree a ete chargee automatiquement au premier demarrage.", example = "true")
    private Boolean defaultLoaded;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getDefaultLoaded() {
        return defaultLoaded;
    }

    public void setDefaultLoaded(Boolean defaultLoaded) {
        this.defaultLoaded = defaultLoaded;
    }
}
