package fr.tiogars.data.softwares.winget.models;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class WingetImportResponse {

    @Schema(description = "Nombre d'applications creees durant l'import.", example = "2")
    private int createdCount;

    @Schema(description = "Nombre d'identifiants Winget ignores (doublons ou invalides).", example = "1")
    private int skippedCount;

    @Schema(description = "Applications Winget creees pendant l'import.")
    private List<Winget> createdItems = new ArrayList<>();

    @Schema(description = "Liste des identifiants Winget ignores (doublons deja existants ou repetes dans l'import).")
    private List<String> skippedWingetIds = new ArrayList<>();

    public int getCreatedCount() {
        return createdCount;
    }

    public void setCreatedCount(int createdCount) {
        this.createdCount = createdCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public List<Winget> getCreatedItems() {
        return createdItems;
    }

    public void setCreatedItems(List<Winget> createdItems) {
        this.createdItems = createdItems;
    }

    public List<String> getSkippedWingetIds() {
        return skippedWingetIds;
    }

    public void setSkippedWingetIds(List<String> skippedWingetIds) {
        this.skippedWingetIds = skippedWingetIds;
    }
}
