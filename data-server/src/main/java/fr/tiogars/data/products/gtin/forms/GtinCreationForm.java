package fr.tiogars.data.products.gtin.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class GtinCreationForm {

    @Schema(description = "Le code GTIN.", example = "0123456789012")
    private String code;

    @Schema(description = "La description du GTIN.", example = "Produit exemple")
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
