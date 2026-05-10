package fr.tiogars.data.dev.docs.model.models;

import java.util.List;

public class ModelImportResult {

    private List<Model> imported;
    private int importedCount;
    private List<String> duplicateNames;
    private int skippedCount;

    public ModelImportResult(List<Model> imported, List<String> duplicateNames) {
        this.imported = imported;
        this.importedCount = imported != null ? imported.size() : 0;
        this.duplicateNames = duplicateNames;
        this.skippedCount = duplicateNames != null ? duplicateNames.size() : 0;
    }

    public List<Model> getImported() {
        return imported;
    }

    public void setImported(List<Model> imported) {
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
}
