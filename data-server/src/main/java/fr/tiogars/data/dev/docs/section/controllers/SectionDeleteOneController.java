package fr.tiogars.data.dev.docs.section.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.section.services.SectionDeleteOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "section", description = "Opérations liées à la gestion des sections.")
public class SectionDeleteOneController {
    
    private final SectionDeleteOneService sectionDeleteOneService;

    public SectionDeleteOneController(SectionDeleteOneService sectionDeleteOneService) {
        this.sectionDeleteOneService = sectionDeleteOneService;
    }

    @DeleteMapping("/section/{id}")
    @Operation(summary = "Supprimer une section", description = "Supprime une section précise par son identifiant. Cette opération est irréversible.")
    public ResponseEntity<Void> deleteSectionById(@PathVariable("id") String sectionId) {
        sectionDeleteOneService.deleteSectionById(sectionId);
        return ResponseEntity.noContent().build();
    }
}
