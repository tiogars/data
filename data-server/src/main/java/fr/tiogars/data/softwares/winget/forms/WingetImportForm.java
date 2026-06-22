package fr.tiogars.data.softwares.winget.forms;

import io.swagger.v3.oas.annotations.media.Schema;

public class WingetImportForm {

    @Schema(description = "Bloc texte contenant un wingetId par ligne.", example = "Microsoft.VisualStudioCode\nNotepad++.Notepad++")
    private String wingetIdsText;

    public String getWingetIdsText() {
        return wingetIdsText;
    }

    public void setWingetIdsText(String wingetIdsText) {
        this.wingetIdsText = wingetIdsText;
    }
}
