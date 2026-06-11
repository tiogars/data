package fr.tiogars.data.vehicles.carmileage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.vehicles.carmileage.models.CarMileageSearchResponse;
import fr.tiogars.data.vehicles.carmileage.services.CarMileageSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "car-mileage", description = "Operations liees aux releves de kilometrage des voitures.")
public class CarMileageSearchController {

    private final CarMileageSearchService carMileageSearchService;

    public CarMileageSearchController(CarMileageSearchService carMileageSearchService) {
        this.carMileageSearchService = carMileageSearchService;
    }

    @GetMapping("/car-mileage/search")
    @Operation(summary = "Rechercher des releves", description = "Cette operation permet de recuperer une liste paginee de releves de kilometrage.")
    public ResponseEntity<CarMileageSearchResponse> searchCarMileages(
        @Parameter(description = "Identifiant optionnel de voiture pour filtrer.", example = "123e4567-e89b-12d3-a456-426614174000")
        @RequestParam(required = false) String carId,
        @Parameter(description = "Index de page (commence a 0).", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Nombre d'elements par page.", example = "10")
        @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("Le parametre page doit etre superieur ou egal a 0.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        }

        if (size > 100) {
            throw new IllegalArgumentException("Le parametre size ne peut pas depasser 100.");
        }

        return ResponseEntity.ok(carMileageSearchService.searchCarMileages(carId, page, size));
    }
}
