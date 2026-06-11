package fr.tiogars.data.vehicles.carmileage.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class CarMileageImportResult {

    @Schema(description = "Liste des releves ajoutes pendant cet import.")
    private List<CarMileage> imported;

    @Schema(description = "Nombre de releves ajoutes.", example = "12")
    private int addedCount;

    @Schema(description = "Nombre total de releves non ajoutes.", example = "3")
    private int notAddedCount;

    @Schema(description = "Nombre de releves non ajoutes car deja presents.", example = "2")
    private int alreadyExistsCount;

    @Schema(description = "Nombre de lignes non ajoutees a cause d'une erreur de validation ou de persistence.", example = "1")
    private int invalidCount;

    public CarMileageImportResult(
        List<CarMileage> imported,
        int addedCount,
        int notAddedCount,
        int alreadyExistsCount,
        int invalidCount
    ) {
        this.imported = imported;
        this.addedCount = addedCount;
        this.notAddedCount = notAddedCount;
        this.alreadyExistsCount = alreadyExistsCount;
        this.invalidCount = invalidCount;
    }

    public List<CarMileage> getImported() {
        return imported;
    }

    public void setImported(List<CarMileage> imported) {
        this.imported = imported;
    }

    public int getAddedCount() {
        return addedCount;
    }

    public void setAddedCount(int addedCount) {
        this.addedCount = addedCount;
    }

    public int getNotAddedCount() {
        return notAddedCount;
    }

    public void setNotAddedCount(int notAddedCount) {
        this.notAddedCount = notAddedCount;
    }

    public int getAlreadyExistsCount() {
        return alreadyExistsCount;
    }

    public void setAlreadyExistsCount(int alreadyExistsCount) {
        this.alreadyExistsCount = alreadyExistsCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public void setInvalidCount(int invalidCount) {
        this.invalidCount = invalidCount;
    }
}
