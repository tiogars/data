package fr.tiogars.data.cave.couleur.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Couleur {

    @Schema(description = "L'identifiant unique.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le nom de la couleur.", example = "Rouge")
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
