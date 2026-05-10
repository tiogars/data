package fr.tiogars.data.dev.docs.model.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class ModelAiTextResponse {

    @Schema(description = "Identifiant du modele source.")
    private String modelId;

    @Schema(description = "Texte formate pour une IA afin de recreer un modele.")
    private String text;

    public ModelAiTextResponse(String modelId, String text) {
        this.modelId = modelId;
        this.text = text;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
