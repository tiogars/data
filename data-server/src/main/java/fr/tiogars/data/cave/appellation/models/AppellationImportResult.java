package fr.tiogars.data.cave.appellation.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class AppellationImportResult {

    @Schema(description = "Liste des appellations ajoutes pendant cet import.")
    private List<Appellation> imported;

    @Schema(description = "Nombre de appellations ajoutes (champ historique).", example = "12")
    private int importedCount;

    @Schema(description = "Liste des noms detectes en doublon (champ historique).")
    private List<String> duplicateNames;

    @Schema(description = "Nombre de appellations non ajoutes (champ historique).", example = "3")
    private int skippedCount;

    @Schema(description = "Nombre de appellations ajoutes.", example = "12")
    private int addedCount;

    @Schema(description = "Nombre total de appellations non ajoutes.", example = "3")
    private int notAddedCount;

    @Schema(description = "Nombre de appellations non ajoutes car deja presents.", example = "2")
    private int alreadyExistsCount;

    @Schema(description = "Nombre de lignes non ajoutees a cause d'une erreur de validation ou de persistence.", example = "1")
    private int invalidCount;

    public AppellationImportResult(List<Appellation> imported, int addedCount, int notAddedCount, int alreadyExistsCount, int invalidCount, List<String> duplicateNames) {
        this.imported = imported;
        this.importedCount = addedCount;
        this.duplicateNames = duplicateNames;
        this.skippedCount = notAddedCount;
        this.addedCount = addedCount;
        this.notAddedCount = notAddedCount;
        this.alreadyExistsCount = alreadyExistsCount;
        this.invalidCount = invalidCount;
    }

    public List<Appellation> getImported() {
        return imported;
    }

    public void setImported(List<Appellation> imported) {
        this.imported = imported;
    }


    public int getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }


    public List<String> getDuplicateNames() {
        return duplicateNames;
    }

    public void setDuplicateNames(List<String> duplicateNames) {
        this.duplicateNames = duplicateNames;
    }


    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
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
