package fr.tiogars.data.dev.docs.section.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.section.services.SectionDeleteAllService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "section", description = "Opérations liées à la gestion des sections.")
public class SectionDeleteAllController {
    
    private final SectionDeleteAllService sectionDeleteAllService;

    public SectionDeleteAllController(SectionDeleteAllService sectionDeleteAllService) {
        this.sectionDeleteAllService = sectionDeleteAllService;
    }

    @DeleteMapping("/section")
    @Operation(summary = "Supprimer toutes les sections", description = "Cette opération permet de supprimer toutes les sections présentes dans l'application. Cette opération est irréversible et doit être utilisée avec précaution.")
    public ResponseEntity<Void> deleteAllSections() {
        sectionDeleteAllService.deleteAllSections();
        return ResponseEntity.noContent().build();
    }
    
}