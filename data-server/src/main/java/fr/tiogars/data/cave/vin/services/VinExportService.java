package fr.tiogars.data.cave.vin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vin.models.VinListResponse;

@Service
public class VinExportService {

    private final VinListService vinListService;

    public VinExportService(VinListService vinListService) {
        this.vinListService = vinListService;
    }

    public VinListResponse exportVins() {
        return vinListService.listVins();
    }
}
