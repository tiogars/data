package fr.tiogars.data.dev.docs.model.forms;

import java.util.List;

import fr.tiogars.data.dev.docs.model.models.ModelAttribute;
import io.swagger.v3.oas.annotations.media.Schema;

public class ModelCreationForm {

    @Schema(description = "Le nom du modele.", example = "Modele Catalogue")
    private String name;

    @Schema(description = "La description du modele.", example = "Structure des donnees de reference")
    private String description;

    @Schema(description = "Collection des attributs du modele.")
    private List<ModelAttribute> modelAttributes;

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

    public List<ModelAttribute> getModelAttributes() {
        return modelAttributes;
    }

    public void setModelAttributes(List<ModelAttribute> modelAttributes) {
        this.modelAttributes = modelAttributes;
    }
}
