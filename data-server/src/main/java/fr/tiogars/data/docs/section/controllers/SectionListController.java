package fr.tiogars.data.docs.section.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.docs.section.models.SectionListResponse;
import fr.tiogars.data.docs.section.services.SectionListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "section", description = "Opérations liées à la gestion des sections.")
public class SectionListController {
    
    private final SectionListService sectionListService;

    public SectionListController(SectionListService sectionListService) {
        this.sectionListService = sectionListService;
    }

    @GetMapping("/section")
    @Operation(summary = "Lister des sections", description = "Cette opération permet de récupérer la liste de toutes les sections présentes dans l'application.")
    public ResponseEntity<SectionListResponse> listSections() {
        return ResponseEntity.ok(sectionListService.listSections());
    }
    
}
