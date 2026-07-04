package fr.tiogars.data.docs.sectiondocument.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.docs.sectiondocument.models.SectionDocument;
import fr.tiogars.data.docs.sectiondocument.services.SectionDocumentCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "section-document", description = "Opérations liées à la gestion des documents de sections.")
public class SectionDocumentCreationController {

    private final SectionDocumentCreationService sectionDocumentCreationService;

    public SectionDocumentCreationController(SectionDocumentCreationService sectionDocumentCreationService) {
        this.sectionDocumentCreationService = sectionDocumentCreationService;
    }

    @PostMapping("/section-document")
    @Operation(summary = "Créer un document", description = "Cette opération permet de créer un document de sections.")
    public ResponseEntity<SectionDocument> create(@RequestBody SectionDocument request) {
        return ResponseEntity.ok(sectionDocumentCreationService.create(request));
    }
}
