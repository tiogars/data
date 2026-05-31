package fr.tiogars.data.docs.section.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.docs.section.forms.SectionCreationForm;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.services.SectionCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur pour la création de sections.
 * Ce contrôleur gère les requêtes liées à la création de nouvelles sections
 * dans l'application.
 */
@RestController()
@Tag(name = "section", description = "Opérations liées à la gestion des sections.")
public class SectionCreationController {

    private final SectionCreationService sectionCreationService;

    public SectionCreationController(SectionCreationService sectionCreationService) {
        this.sectionCreationService = sectionCreationService;
    }

    /**
     * Créer une nouvelle section.
     */
    @PostMapping(value = "/section")
    @Operation(summary = "Créer une nouvelle section", description = "Cette opération permet de créer une nouvelle section dans l'application.")
    public ResponseEntity<Section> createSection(@RequestBody SectionCreationForm sectionCreationForm) {
        return ResponseEntity.ok(sectionCreationService.createSection(sectionCreationForm));
    }
}
