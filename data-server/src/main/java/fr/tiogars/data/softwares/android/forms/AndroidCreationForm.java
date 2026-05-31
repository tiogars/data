package fr.tiogars.data.softwares.android.forms;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class AndroidCreationForm {

    @Schema(description = "Le nom de l'application Android.", example = "Google Keep")
    private String name;

    @Schema(description = "Le nom du package Android.", example = "com.google.android.keep")
    private String packageName;

    @Schema(description = "Les categories associees a l'application.", example = "[\"productivity\", \"notes\"]")
    private List<String> category;

    @Schema(description = "La description de l'application Android.", example = "Application de prise de notes")
    private String description;

    @Schema(description = "L'icone de l'application au format URL ou base64.")
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}