package fr.tiogars.data.dev.docs.footerlink.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class FooterLinkCreationForm {

    @Schema(description = "Le libellé affiché pour le lien.", example = "React")
    private String label;

    @Schema(description = "L'URL cible du lien.", example = "https://react.dev/")
    private String url;

    @Schema(description = "La clé d'icône utilisée par l'application web.", example = "react")
    private String icon;

    @Schema(description = "L'ordre d'affichage du lien dans le footer.", example = "10")
    private Integer displayOrder;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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