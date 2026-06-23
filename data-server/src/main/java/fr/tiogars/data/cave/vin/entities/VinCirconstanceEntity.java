package fr.tiogars.data.cave.vin.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vin_circonstance")
public class VinCirconstanceEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "vin_id", nullable = false)
    private String vinId;

    @Column(name = "circonstance_id", nullable = false)
    private String circonstanceId;

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

    public String getCirconstanceId() {
        return circonstanceId;
    }

    public void setCirconstanceId(String circonstanceId) {
        this.circonstanceId = circonstanceId;
    }
}
