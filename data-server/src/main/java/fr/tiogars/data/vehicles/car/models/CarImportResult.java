package fr.tiogars.data.vehicles.car.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class CarImportResult {

    @Schema(description = "Liste des voitures ajoutees pendant cet import.")
    private List<Car> imported;

    @Schema(description = "Nombre de voitures ajoutees.", example = "12")
    private int addedCount;

    @Schema(description = "Nombre total de voitures non ajoutees.", example = "3")
    private int notAddedCount;

    @Schema(description = "Nombre de voitures non ajoutees car deja presentes.", example = "2")
    private int alreadyExistsCount;

    @Schema(description = "Nombre de lignes non ajoutees a cause d'une erreur de validation ou de persistence.", example = "1")
    private int invalidCount;

    public CarImportResult(
        List<Car> imported,
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

    public List<Car> getImported() {
        return imported;
    }

    public void setImported(List<Car> imported) {
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
