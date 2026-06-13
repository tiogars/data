package fr.tiogars.data.softwares.android.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.android.repositories.AndroidRepository;
import fr.tiogars.data.sync.services.SyncDeletionEventService;

@Service
public class AndroidDeleteAllService {

    private final AndroidRepository androidRepository;
    private final SyncDeletionEventService syncDeletionEventService;

    public AndroidDeleteAllService(AndroidRepository androidRepository, SyncDeletionEventService syncDeletionEventService) {
        this.androidRepository = androidRepository;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public void deleteAllAndroids() {
        List<String> ids = androidRepository.findAll().stream().map(entity -> entity.getId()).toList();
        androidRepository.deleteAllInBatch();
        syncDeletionEventService.recordDeletions("android", ids);
    }
}