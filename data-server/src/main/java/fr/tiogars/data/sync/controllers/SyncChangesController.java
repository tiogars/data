package fr.tiogars.data.sync.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.sync.services.SyncChangesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "sync", description = "Operations de synchronisation incrementale mobile.")
public class SyncChangesController {

    private final SyncChangesService syncChangesService;

    public SyncChangesController(SyncChangesService syncChangesService) {
        this.syncChangesService = syncChangesService;
    }

    @GetMapping("/api/v1/sync/{domain}/changes")
    @Operation(
        summary = "Recuperer les changements incrementaux d'un domaine",
        description = "Retourne les elements crees/modifies, les identifiants supprimes et un curseur opaque de reprise."
    )
    public ResponseEntity<?> getChanges(
        @Parameter(description = "Domaine cible: gtin, car, car-mileage, android, winget", example = "gtin")
        @PathVariable String domain,
        @Parameter(description = "Token opaque de reprise.")
        @RequestParam(required = false) String cursor,
        @Parameter(description = "Repere temporel optionnel (compatibilite), format ISO-8601.", example = "2026-06-13T10:00:00Z")
        @RequestParam(required = false) String updatedAfter,
        @Parameter(description = "Taille de page demandee (max 500).", example = "100")
        @RequestParam(required = false) Integer size
    ) {
        return switch (domain) {
            case "gtin" -> ResponseEntity.ok(syncChangesService.getGtinChanges(cursor, updatedAfter, size));
            case "car" -> ResponseEntity.ok(syncChangesService.getCarChanges(cursor, updatedAfter, size));
            case "car-mileage" -> ResponseEntity.ok(syncChangesService.getCarMileageChanges(cursor, updatedAfter, size));
            case "android" -> ResponseEntity.ok(syncChangesService.getAndroidChanges(cursor, updatedAfter, size));
            case "winget" -> ResponseEntity.ok(syncChangesService.getWingetChanges(cursor, updatedAfter, size));
            default -> throw new IllegalArgumentException("Domaine de synchronisation non supporte: " + domain);
        };
    }
}
