package fr.tiogars.data.cave.circonstance.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Circonstance {

    @Schema(description = "L'identifiant unique.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le nom de la circonstance.", example = "Anniversaire")
    private String name;

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

}
