package fr.tiogars.data.softwares.winget.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.softwares.winget.repositories.WingetRepository;
import fr.tiogars.data.sync.services.SyncDeletionEventService;

@Service
public class WingetDeleteOneService {

    private final WingetRepository wingetRepository;
    private final SyncDeletionEventService syncDeletionEventService;

    public WingetDeleteOneService(WingetRepository wingetRepository, SyncDeletionEventService syncDeletionEventService) {
        this.wingetRepository = wingetRepository;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public void deleteWinget(String id) {
        if (!wingetRepository.existsById(id)) {
            throw new DataNotFoundException("Application Winget introuvable.");
        }

        wingetRepository.deleteById(id);
        syncDeletionEventService.recordDeletion("winget", id);
    }
}
