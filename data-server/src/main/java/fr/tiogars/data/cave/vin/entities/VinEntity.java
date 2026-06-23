package fr.tiogars.data.cave.vin.entities;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "vin")
public class VinEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "appellation_id")
    private String appellationId;

    @Column(name = "couleur_id")
    private String couleurId;

    @Column(name = "type_vin_id")
    private String typeVinId;

    @Column(name = "maison_id")
    private String maisonId;

    @Column(name = "vin_nom_id")
    private String vinNomId;

    @Column(name = "contenant_id")
    private String contenantId;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "degorgement_mois")
    private Integer degorgementMois;

    @Column(name = "degorgement_annee")
    private Integer degorgementAnnee;

    @Column(name = "dosage_grammes_par_litre")
    private BigDecimal dosageGrammesParLitre;

    @Column(name = "commune")
    private String commune;

    @Column(name = "region")
    private String region;

    @Column(name = "commentaires", columnDefinition = "TEXT")
    private String commentaires;

    @Column(name = "accords_mets_vins", columnDefinition = "TEXT")
    private String accordsMetsVins;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
