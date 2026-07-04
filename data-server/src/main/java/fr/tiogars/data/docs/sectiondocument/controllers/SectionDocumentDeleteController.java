package fr.tiogars.data.docs.sectiondocument.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.docs.sectiondocument.services.SectionDocumentDeleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "section-document", description = "Opérations liées à la gestion des documents de sections.")
public class SectionDocumentDeleteController {

    private final SectionDocumentDeleteService sectionDocumentDeleteService;

    public SectionDocumentDeleteController(SectionDocumentDeleteService sectionDocumentDeleteService) {
        this.sectionDocumentDeleteService = sectionDocumentDeleteService;
    }

    @DeleteMapping("/section-document/{id}")
    @Operation(summary = "Supprimer un document", description = "Cette opération permet de supprimer un document de sections.")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sectionDocumentDeleteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
