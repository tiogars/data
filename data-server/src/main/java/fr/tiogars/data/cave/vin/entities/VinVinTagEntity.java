package fr.tiogars.data.cave.vin.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vin_vin_tag")
public class VinVinTagEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "vin_id", nullable = false)
    private String vinId;

    @Column(name = "vin_tag_id", nullable = false)
    private String vinTagId;

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

    public String getVinTagId() {
        return vinTagId;
    }

    public void setVinTagId(String vinTagId) {
        this.vinTagId = vinTagId;
    }
}
