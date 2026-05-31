package fr.tiogars.data.products.brand.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class BrandImportResult {

    @Schema(description = "Liste des marques ajoutees pendant cet import.")
    private List<Brand> imported;

    @Schema(description = "Nombre de marques ajoutees (champ historique).", example = "12")
    private int importedCount;

    @Schema(description = "Liste des noms detectes en doublon (champ historique).")
    private List<String> duplicateNames;

    @Schema(description = "Nombre de marques non ajoutees (champ historique).", example = "3")
    private int skippedCount;

    @Schema(description = "Nombre de marques ajoutees.", example = "12")
    private int addedCount;

    @Schema(description = "Nombre total de marques non ajoutees.", example = "3")
    private int notAddedCount;

    @Schema(description = "Nombre de marques non ajoutees car deja presentes.", example = "2")
    private int alreadyExistsCount;

    @Schema(description = "Nombre de lignes non ajoutees a cause d'une erreur de validation ou de persistence.", example = "1")
    private int invalidCount;

    public BrandImportResult(
        List<Brand> imported,
        int addedCount,
        int notAddedCount,
        int alreadyExistsCount,
        int invalidCount,
        List<String> duplicateNames
    ) {
        this.imported = imported;
        this.importedCount = addedCount;
        this.duplicateNames = duplicateNames;
        this.skippedCount = notAddedCount;
        this.addedCount = addedCount;
        this.notAddedCount = notAddedCount;
        this.alreadyExistsCount = alreadyExistsCount;
        this.invalidCount = invalidCount;
    }

    public List<Brand> getImported() {
        return imported;
    }

    public void setImported(List<Brand> imported) {
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
