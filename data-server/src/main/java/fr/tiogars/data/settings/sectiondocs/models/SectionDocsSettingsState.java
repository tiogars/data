package fr.tiogars.data.settings.sectiondocs.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class SectionDocsSettingsState {

    @Schema(description = "Liste des paramétrages de chemin documentaire par section racine.")
    private List<SectionDocsSetting> items;

    public SectionDocsSettingsState() {
    }

    public SectionDocsSettingsState(List<SectionDocsSetting> items) {
        this.items = items;
    }

    public List<SectionDocsSetting> getItems() {
        return items;
    }

    public void setItems(List<SectionDocsSetting> items) {
        this.items = items;
    }
}