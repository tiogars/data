package fr.tiogars.data.cave.vin.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vin_cepage")
public class VinCepageEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "vin_id", nullable = false)
    private String vinId;

    @Column(name = "cepage_id", nullable = false)
    private String cepageId;

    @Column(name = "pourcentage")
    private Integer pourcentage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVinId() {
        return vinId;
    }

    public void setVinId(String vinId) {
        this.vinId = vinId;
    }

    public String getCepageId() {
        return cepageId;
    }

    public void setCepageId(String cepageId) {
        this.cepageId = cepageId;
    }

    public Integer getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Integer pourcentage) {
        this.pourcentage = pourcentage;
    }
}
