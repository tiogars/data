package fr.tiogars.data.cave.contenant.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Contenant {
    @Schema(description = "L'identifiant unique.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;
    @Schema(description = "Le nom du contenant.", example = "Bouteille")
    private String name;
    @Schema(description = "Le volume du contenant en centilitres.", example = "75")
    private Integer volumeCl;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    public Integer getVolumeCl() { return volumeCl; }
    public void setVolumeCl(Integer volumeCl) { this.volumeCl = volumeCl; }

}
