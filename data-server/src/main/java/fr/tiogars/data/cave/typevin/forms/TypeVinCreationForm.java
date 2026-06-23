package fr.tiogars.data.cave.typevin.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class TypeVinCreationForm {
    @Schema(description = "Le nom du type de vin.", example = "Brut")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
