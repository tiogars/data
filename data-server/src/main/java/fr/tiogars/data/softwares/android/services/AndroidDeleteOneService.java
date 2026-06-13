package fr.tiogars.data.softwares.android.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;
import fr.tiogars.data.sync.services.SyncDeletionEventService;

@Service
public class AndroidDeleteOneService {

    private final AndroidRepository androidRepository;
    private final SyncDeletionEventService syncDeletionEventService;

    public AndroidDeleteOneService(AndroidRepository androidRepository, SyncDeletionEventService syncDeletionEventService) {
        this.androidRepository = androidRepository;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public void deleteAndroid(String id) {
        if (!androidRepository.existsById(id)) {
            throw new DataNotFoundException("Application Android introuvable.");
        }
        androidRepository.deleteById(id);
        syncDeletionEventService.recordDeletion("android", id);
    }
}