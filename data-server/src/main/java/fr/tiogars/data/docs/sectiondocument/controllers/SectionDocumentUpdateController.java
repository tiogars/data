package fr.tiogars.data.docs.sectiondocument.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.docs.sectiondocument.models.SectionDocument;
import fr.tiogars.data.docs.sectiondocument.services.SectionDocumentUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "section-document", description = "Opérations liées à la gestion des documents de sections.")
public class SectionDocumentUpdateController {

    private final SectionDocumentUpdateService sectionDocumentUpdateService;

    public SectionDocumentUpdateController(SectionDocumentUpdateService sectionDocumentUpdateService) {
        this.sectionDocumentUpdateService = sectionDocumentUpdateService;
    }

    @PutMapping("/section-document/{id}")
    @Operation(summary = "Mettre à jour un document", description = "Cette opération permet de mettre à jour un document de sections.")
    public ResponseEntity<SectionDocument> update(@PathVariable String id, @RequestBody SectionDocument request) {
        return ResponseEntity.ok(sectionDocumentUpdateService.update(id, request));
    }
}
