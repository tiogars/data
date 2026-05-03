package fr.tiogars.data.dev.docs.section.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.section.models.Section;
import fr.tiogars.data.dev.docs.section.services.SectionUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "section", description = "Opérations liées à la gestion des sections.")
public class SectionUpdateController {
    private final SectionUpdateService sectionUpdateService;

    public SectionUpdateController(SectionUpdateService sectionUpdateService) {
        this.sectionUpdateService = sectionUpdateService;
    }

    @PutMapping("/section/{id}")
    @Operation(summary = "Mettre à jour une section", description = "Cette opération permet de mettre à jour une section existante à partir de son identifiant.")
    public ResponseEntity<Section> updateSection(@PathVariable String id, @RequestBody Section sectionUpdate) {
        Section updatedSection = sectionUpdateService.updateSection(id, sectionUpdate);
        return ResponseEntity.ok(updatedSection);
    }
}
