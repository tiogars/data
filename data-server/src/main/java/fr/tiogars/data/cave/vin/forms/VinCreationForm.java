package fr.tiogars.data.cave.vin.forms;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class VinCreationForm {

    @Schema(description = "Identifiant de l'appellation.", nullable = true)
    private String appellationId;

    @Schema(description = "Identifiant de la couleur.", nullable = true)
    private String couleurId;

    @Schema(description = "Identifiant du type de vin.", nullable = true)
    private String typeVinId;

    @Schema(description = "Identifiant de la maison.", nullable = true)
    private String maisonId;

    @Schema(description = "Identifiant du nom du vin.", nullable = true)
    private String vinNomId;

    @Schema(description = "Identifiant du contenant.", nullable = true)
    private String contenantId;

    @Schema(description = "Millesime du vin.", example = "2022", nullable = true)
    private Integer annee;

    @Schema(description = "Mois de degorgement du vin mousseux.", example = "4", nullable = true)
    private Integer degorgementMois;

    @Schema(description = "Annee de degorgement du vin mousseux.", example = "2025", nullable = true)
    private Integer degorgementAnnee;

    @Schema(description = "Dosage en grammes par litre.", example = "7.5", nullable = true)
    private BigDecimal dosageGrammesParLitre;

    @Schema(description = "Commune du vin.", example = "Beaune", nullable = true)
    private String commune;

    @Schema(description = "Region du vin.", example = "Bourgogne", nullable = true)
    private String region;

    @Schema(description = "Commentaires de degustation.", nullable = true)
    private String commentaires;

    @Schema(description = "Accords mets et vins.", nullable = true)
    private String accordsMetsVins;

    @Schema(description = "Liste des cepages composant le vin.")
    private List<VinCepageEntryForm> cepages;

    @Schema(description = "Liste des identifiants de circonstances.")
    private List<String> circonstanceIds;

    @Schema(description = "Liste des identifiants de tags.")
    private List<String> tagIds;

    public String getAppellationId() {
        return appellationId;
    }

    public void setAppellationId(String appellationId) {
        this.appellationId = appellationId;
    }

    public String getCouleurId() {
        return couleurId;
    }

    public void setCouleurId(String couleurId) {
        this.couleurId = couleurId;
    }

    public String getTypeVinId() {
        return typeVinId;
    }

    public void setTypeVinId(String typeVinId) {
        this.typeVinId = typeVinId;
    }

    public String getMaisonId() {
        return maisonId;
    }

    public void setMaisonId(String maisonId) {
        this.maisonId = maisonId;
    }

    public String getVinNomId() {
        return vinNomId;
    }

    public void setVinNomId(String vinNomId) {
        this.vinNomId = vinNomId;
    }

    public String getContenantId() {
        return contenantId;
    }

    public void setContenantId(String contenantId) {
        this.contenantId = contenantId;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getDegorgementMois() {
        return degorgementMois;
    }

    public void setDegorgementMois(Integer degorgementMois) {
        this.degorgementMois = degorgementMois;
    }

    public Integer getDegorgementAnnee() {
        return degorgementAnnee;
    }

    public void setDegorgementAnnee(Integer degorgementAnnee) {
        this.degorgementAnnee = degorgementAnnee;
    }

    public BigDecimal getDosageGrammesParLitre() {
        return dosageGrammesParLitre;
    }

    public void setDosageGrammesParLitre(BigDecimal dosageGrammesParLitre) {
        this.dosageGrammesParLitre = dosageGrammesParLitre;
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

    public List<VinCepageEntryForm> getCepages() {
        return cepages;
    }

    public void setCepages(List<VinCepageEntryForm> cepages) {
        this.cepages = cepages;
    }

    public List<String> getCirconstanceIds() {
        return circonstanceIds;
    }

    public void setCirconstanceIds(List<String> circonstanceIds) {
        this.circonstanceIds = circonstanceIds;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }
}
