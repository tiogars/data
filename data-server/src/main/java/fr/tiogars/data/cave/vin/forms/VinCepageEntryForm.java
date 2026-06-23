package fr.tiogars.data.cave.vin.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class VinCepageEntryForm {

    @Schema(description = "Identifiant du cepage.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String cepageId;

    @Schema(description = "Pourcentage du cepage dans l'assemblage.", example = "60")
    private Integer pourcentage;

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
