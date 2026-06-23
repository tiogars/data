package fr.tiogars.data.cave.vin.models;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class Vin {

    @Schema(description = "Identifiant unique du vin.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Identifiant de l'appellation.", nullable = true)
    private String appellationId;

    @Schema(description = "Nom de l'appellation.", nullable = true)
    private String appellationName;

    @Schema(description = "Identifiant de la couleur.", nullable = true)
    private String couleurId;

    @Schema(description = "Nom de la couleur.", nullable = true)
    private String couleurName;

    @Schema(description = "Identifiant du type de vin.", nullable = true)
    private String typeVinId;

    @Schema(description = "Nom du type de vin.", nullable = true)
    private String typeVinName;

    @Schema(description = "Identifiant de la maison.", nullable = true)
    private String maisonId;

    @Schema(description = "Nom de la maison.", nullable = true)
    private String maisonName;

    @Schema(description = "Identifiant du nom du vin.", nullable = true)
    private String vinNomId;

    @Schema(description = "Nom du vin.", nullable = true)
    private String vinNomName;

    @Schema(description = "Identifiant du contenant.", nullable = true)
    private String contenantId;

    @Schema(description = "Nom du contenant.", nullable = true)
    private String contenantName;

    @Schema(description = "Annee du vin.", example = "2022", nullable = true)
    private Integer annee;

    @Schema(description = "Commune du vin.", example = "Beaune", nullable = true)
    private String commune;

    @Schema(description = "Region du vin.", example = "Bourgogne", nullable = true)
    private String region;

    @Schema(description = "Commentaires de degustation.", nullable = true)
    private String commentaires;

    @Schema(description = "Accords mets et vins.", nullable = true)
    private String accordsMetsVins;

    @Schema(description = "Liste des cepages du vin.")
    private List<VinCepageEntry> cepages = new ArrayList<>();

    @Schema(description = "Liste des identifiants de circonstances.")
    private List<String> circonstances = new ArrayList<>();

    @Schema(description = "Liste des libelles de circonstances.")
    private List<String> circonstanceNames = new ArrayList<>();

    @Schema(description = "Liste des identifiants de tags.")
    private List<String> tags = new ArrayList<>();

    @Schema(description = "Liste des libelles de tags.")
    private List<String> tagNames = new ArrayList<>();

    @Schema(description = "Date de creation ISO-8601.", nullable = true)
    private String createdAt;

    @Schema(description = "Date de mise a jour ISO-8601.", nullable = true)
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppellationId() {
        return appellationId;
    }

    public void setAppellationId(String appellationId) {
        this.appellationId = appellationId;
    }

    public String getAppellationName() {
        return appellationName;
    }

    public void setAppellationName(String appellationName) {
        this.appellationName = appellationName;
    }

    public String getCouleurId() {
        return couleurId;
    }

    public void setCouleurId(String couleurId) {
        this.couleurId = couleurId;
    }

    public String getCouleurName() {
        return couleurName;
    }

    public void setCouleurName(String couleurName) {
        this.couleurName = couleurName;
    }

    public String getTypeVinId() {
        return typeVinId;
    }

    public void setTypeVinId(String typeVinId) {
        this.typeVinId = typeVinId;
    }

    public String getTypeVinName() {
        return typeVinName;
    }

    public void setTypeVinName(String typeVinName) {
        this.typeVinName = typeVinName;
    }

    public String getMaisonId() {
        return maisonId;
    }

    public void setMaisonId(String maisonId) {
        this.maisonId = maisonId;
    }

    public String getMaisonName() {
        return maisonName;
    }

    public void setMaisonName(String maisonName) {
        this.maisonName = maisonName;
    }

    public String getVinNomId() {
        return vinNomId;
    }

    public void setVinNomId(String vinNomId) {
        this.vinNomId = vinNomId;
    }

    public String getVinNomName() {
        return vinNomName;
    }

    public void setVinNomName(String vinNomName) {
        this.vinNomName = vinNomName;
    }

    public String getContenantId() {
        return contenantId;
    }

    public void setContenantId(String contenantId) {
        this.contenantId = contenantId;
    }

    public String getContenantName() {
        return contenantName;
    }

    public void setContenantName(String contenantName) {
        this.contenantName = contenantName;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public String getAccordsMetsVins() {
        return accordsMetsVins;
    }

    public void setAccordsMetsVins(String accordsMetsVins) {
        this.accordsMetsVins = accordsMetsVins;
    }

    public List<VinCepageEntry> getCepages() {
        return cepages;
    }

    public void setCepages(List<VinCepageEntry> cepages) {
        this.cepages = cepages != null ? cepages : new ArrayList<>();
    }

    public List<String> getCirconstances() {
        return circonstances;
    }

    public void setCirconstances(List<String> circonstances) {
        this.circonstances = circonstances != null ? circonstances : new ArrayList<>();
    }

    public List<String> getCirconstanceNames() {
        return circonstanceNames;
    }

    public void setCirconstanceNames(List<String> circonstanceNames) {
        this.circonstanceNames = circonstanceNames != null ? circonstanceNames : new ArrayList<>();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames != null ? tagNames : new ArrayList<>();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
