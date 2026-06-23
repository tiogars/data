package fr.tiogars.data.cave.contenant.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class ContenantCreationForm { @Schema(description = "Le nom du contenant.", example = "Bouteille") private String name; @Schema(description = "Le volume du contenant en centilitres.", example = "75") private Integer volumeCl; 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getVolumeCl() { return volumeCl; }
    public void setVolumeCl(Integer volumeCl) { this.volumeCl = volumeCl; }
 }
