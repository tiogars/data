package fr.tiogars.data.settings.urlmanager.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.settings.urlmanager.models.UrlManagerState;

@Service
public class UrlManagerImportService {

    private final UrlManagerStateService urlManagerStateService;

    public UrlManagerImportService(UrlManagerStateService urlManagerStateService) {
        this.urlManagerStateService = urlManagerStateService;
    }

    public UrlManagerState importState(UrlManagerState state) {
        return urlManagerStateService.replaceState(state);
    }
}
