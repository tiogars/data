package fr.tiogars.data.dev.docs.menuitem.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class MenuItemCreationForm {

    @Schema(description = "Le libelle affiche dans le menu.", example = "Sections")
    private String label;

    @Schema(description = "Le chemin de navigation React Router.", example = "/section")
    private String path;

    @Schema(description = "La cle d'icone MUI utilisee par l'application web.", example = "inbox")
    private String icon;

    @Schema(description = "L'ordre d'affichage de l'entree dans le menu.", example = "10")
    private Integer displayOrder;

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
}
