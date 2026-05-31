package fr.tiogars.data.settings.urlmanager.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class UrlManagerState {

    @Schema(description = "Liste des URLs gerees.")
    private List<ManagedUrl> urls;

    @Schema(description = "Liste des cartes d'affichage pour la page d'accueil.")
    private List<UrlCardConfig> cards;

    public UrlManagerState() {
    }

    public UrlManagerState(List<ManagedUrl> urls, List<UrlCardConfig> cards) {
        this.urls = urls;
        this.cards = cards;
    }

    public List<ManagedUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<ManagedUrl> urls) {
        this.urls = urls;
    }

    public List<UrlCardConfig> getCards() {
        return cards;
    }

    public void setCards(List<UrlCardConfig> cards) {
        this.cards = cards;
    }
}
