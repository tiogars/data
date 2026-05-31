package fr.tiogars.data.softwares.android.models;

import java.util.List;

public class AndroidImportResult {

    private List<Android> imported;
    private int importedCount;
    private List<String> duplicatePackageNames;
    private int skippedCount;

    public AndroidImportResult(List<Android> imported, List<String> duplicatePackageNames) {
        this.imported = imported;
        this.importedCount = imported != null ? imported.size() : 0;
        this.duplicatePackageNames = duplicatePackageNames;
        this.skippedCount = duplicatePackageNames != null ? duplicatePackageNames.size() : 0;
    }

    public List<Android> getImported() {
        return imported;
    }

    public void setImported(List<Android> imported) {
        this.imported = imported;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }

    public List<String> getDuplicatePackageNames() {
        return duplicatePackageNames;
    }

    public void setDuplicatePackageNames(List<String> duplicatePackageNames) {
        this.duplicatePackageNames = duplicatePackageNames;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }
}