package fr.tiogars.data.products.gtin.models;

import java.util.List;

public class GtinImportResult {

    private List<Gtin> imported;
    private int importedCount;
    private List<String> duplicateCodes;
    private int skippedCount;

    public GtinImportResult(List<Gtin> imported, List<String> duplicateCodes) {
        this.imported = imported;
        this.importedCount = imported != null ? imported.size() : 0;
        this.duplicateCodes = duplicateCodes;
        this.skippedCount = duplicateCodes != null ? duplicateCodes.size() : 0;
    }

    public List<Gtin> getImported() {
        return imported;
    }

    public void setImported(List<Gtin> imported) {
        this.imported = imported;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }

    public List<String> getDuplicateCodes() {
        return duplicateCodes;
    }

    public void setDuplicateCodes(List<String> duplicateCodes) {
        this.duplicateCodes = duplicateCodes;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }
}
