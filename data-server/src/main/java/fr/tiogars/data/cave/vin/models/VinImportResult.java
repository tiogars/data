package fr.tiogars.data.cave.vin.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class VinImportResult {

    @Schema(description = "Liste des vins importes.")
    private List<Vin> imported;

    @Schema(description = "Nombre de vins importes (champ historique).", example = "3")
    private int importedCount;

    @Schema(description = "Nombre de vins non ajoutes (champ historique).", example = "1")
    private int notAddedCount;

    @Schema(description = "Nombre de vins ajoutes.", example = "3")
    private int addedCount;

    @Schema(description = "Nombre de vins deja presents et ignores.", example = "0")
    private int alreadyExistsCount;

    @Schema(description = "Nombre de lignes invalides ignorees.", example = "1")
    private int invalidCount;

    public VinImportResult(List<Vin> imported, int addedCount, int notAddedCount, int alreadyExistsCount, int invalidCount) {
        this.imported = imported;
        this.importedCount = addedCount;
        this.notAddedCount = notAddedCount;
        this.addedCount = addedCount;
        this.alreadyExistsCount = alreadyExistsCount;
        this.invalidCount = invalidCount;
    }

    public List<Vin> getImported() {
        return imported;
    }

    public void setImported(List<Vin> imported) {
        this.imported = imported;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }

    public int getNotAddedCount() {
        return notAddedCount;
    }

    public void setNotAddedCount(int notAddedCount) {
        this.notAddedCount = notAddedCount;
    }

    public int getAddedCount() {
        return addedCount;
    }

    public void setAddedCount(int addedCount) {
        this.addedCount = addedCount;
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
