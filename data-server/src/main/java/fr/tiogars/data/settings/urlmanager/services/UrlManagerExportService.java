package fr.tiogars.data.settings.urlmanager.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.settings.urlmanager.models.UrlManagerState;

@Service
public class UrlManagerExportService {

    private final UrlManagerStateService urlManagerStateService;

    public UrlManagerExportService(UrlManagerStateService urlManagerStateService) {
        this.urlManagerStateService = urlManagerStateService;
    }

    public UrlManagerState exportState() {
        return urlManagerStateService.getState();
    }
}
