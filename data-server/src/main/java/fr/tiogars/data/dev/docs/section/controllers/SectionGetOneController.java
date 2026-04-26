package fr.tiogars.data.dev.docs.section.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.section.models.Section;
import fr.tiogars.data.dev.docs.section.services.SectionGetOneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "section", description = "Opérations liées à la gestion des sections.")
public class SectionGetOneController {
    private final SectionGetOneService sectionGetOneService;

    public SectionGetOneController(SectionGetOneService sectionGetOneService) {
        this.sectionGetOneService = sectionGetOneService;
    }

    @GetMapping("/section/{id}")
    @Operation(summary = "Récupérer une section par son identifiant", description = "Cette opération permet de récupérer une section à partir de son identifiant.")
    public ResponseEntity<Section> getSectionById(@PathVariable String id) {
        Section section = sectionGetOneService.getSectionById(id);
        return ResponseEntity.ok(section);
    }
}
