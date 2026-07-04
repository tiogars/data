package fr.tiogars.data.settings.sectiondocs.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.settings.sectiondocs.models.SectionDocsSettingsState;
import fr.tiogars.data.settings.sectiondocs.services.SectionDocsSettingsStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "section-docs-settings", description = "Paramétrage des chemins documentaires des sections racines.")
public class SectionDocsSettingsStateController {

    private final SectionDocsSettingsStateService sectionDocsSettingsStateService;

    public SectionDocsSettingsStateController(SectionDocsSettingsStateService sectionDocsSettingsStateService) {
        this.sectionDocsSettingsStateService = sectionDocsSettingsStateService;
    }

    @GetMapping("/section-docs-settings/state")
    @Operation(
        operationId = "getSectionDocsSettingsState",
        summary = "Lire le paramétrage documentaire des sections",
        description = "Retourne les chemins configurés sous volumes/docs pour les sections racines."
    )
    public ResponseEntity<SectionDocsSettingsState> getState() {
        return ResponseEntity.ok(sectionDocsSettingsStateService.getState());
    }

    @PutMapping("/section-docs-settings/state")
    @Operation(
        operationId = "updateSectionDocsSettingsState",
        summary = "Mettre à jour le paramétrage documentaire des sections",
        description = "Remplace l'ensemble des chemins configurés pour les sections racines."
    )
    public ResponseEntity<SectionDocsSettingsState> updateState(@RequestBody SectionDocsSettingsState state) {
        return ResponseEntity.ok(sectionDocsSettingsStateService.replaceState(state));
    }
}