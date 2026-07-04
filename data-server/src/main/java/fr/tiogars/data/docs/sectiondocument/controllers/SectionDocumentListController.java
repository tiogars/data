package fr.tiogars.data.docs.sectiondocument.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.docs.sectiondocument.models.SectionDocumentListResponse;
import fr.tiogars.data.docs.sectiondocument.services.SectionDocumentListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "section-document", description = "Opérations liées à la gestion des documents de sections.")
public class SectionDocumentListController {

    private final SectionDocumentListService sectionDocumentListService;

    public SectionDocumentListController(SectionDocumentListService sectionDocumentListService) {
        this.sectionDocumentListService = sectionDocumentListService;
    }

    @GetMapping("/section-document/list")
    @Operation(summary = "Lister les documents", description = "Cette opération permet de récupérer la liste des documents de sections.")
    public ResponseEntity<SectionDocumentListResponse> listDocuments() {
        return ResponseEntity.ok(sectionDocumentListService.listDocuments());
    }
}
