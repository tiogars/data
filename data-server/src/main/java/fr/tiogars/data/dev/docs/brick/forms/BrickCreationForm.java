package fr.tiogars.data.dev.docs.brick.forms;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class BrickCreationForm {

    @Schema(description = "Numero de reference de la brique.", example = "60284")
    private String number;

    @Schema(description = "Titre de la brique.", example = "Le camion de chantier")
    private String title;

    @Schema(description = "Tags de classification.", example = "[\"city\",\"truck\"]")
    private List<String> tags;

    @Schema(description = "Image en data URL base64.", example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...")
    private String imageBase64;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
