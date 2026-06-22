package fr.tiogars.data.softwares.winget.models;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import fr.tiogars.data.sync.services.SyncUpdatedItem;

public class Winget implements SyncUpdatedItem {

    @Schema(description = "L'identifiant unique de l'application Windows.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Le nom de l'application Windows.", example = "Notepad++")
    private String name;

    @Schema(description = "La description de l'application.", example = "Notepad++ is a free source code editor and Notepad replacement that supports several programming languages.")
    private String description;

    @Schema(description = "L'identifiant Winget de l'application.", example = "Notepad++.Notepad++")
    private String wingetId;

    @Schema(description = "La commande d'installation Winget.", example = "winget install -e --id Notepad++.Notepad++")
    private String installCommand;

    @Schema(description = "Les tags associes a l'application.", example = "[\"editor\", \"windows\"]")
    private List<String> tags;

    @Schema(description = "Date de derniere mise a jour de l'element.", example = "2026-06-13T11:45:00Z")
    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWingetId() {
        return wingetId;
    }

    public void setWingetId(String wingetId) {
        this.wingetId = wingetId;
    }

    public String getInstallCommand() {
        return installCommand;
    }

    public void setInstallCommand(String installCommand) {
        this.installCommand = installCommand;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
