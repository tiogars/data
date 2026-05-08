package fr.tiogars.data.dev.docs.brick.controllers;

import java.io.IOException;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.brick.services.BrickPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "brick")
public class BrickPdfController {

    private final BrickPdfService brickPdfService;

    public BrickPdfController(BrickPdfService brickPdfService) {
        this.brickPdfService = brickPdfService;
    }

    @GetMapping(value = "/brick/pdf", produces = "application/pdf")
    @Operation(
        summary = "Exporter le catalogue PDF",
        description = "Génère et retourne un fichier PDF contenant le catalogue complet des briques."
    )
    public ResponseEntity<byte[]> generatePdf() throws IOException {
        byte[] pdf = brickPdfService.generateCatalog();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
            ContentDisposition.attachment().filename("bricks.pdf").build()
        );

        return ResponseEntity.ok()
            .headers(headers)
            .body(pdf);
    }
}
