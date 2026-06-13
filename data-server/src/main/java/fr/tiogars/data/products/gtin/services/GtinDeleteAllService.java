package fr.tiogars.data.products.gtin.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.repositories.GtinRepository;
import fr.tiogars.data.sync.services.SyncDeletionEventService;

@Service
public class GtinDeleteAllService {

    private final GtinRepository gtinRepository;
    private final SyncDeletionEventService syncDeletionEventService;

    public GtinDeleteAllService(GtinRepository gtinRepository, SyncDeletionEventService syncDeletionEventService) {
        this.gtinRepository = gtinRepository;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public void deleteAllGtins() {
        List<String> ids = gtinRepository.findAll().stream().map(entity -> entity.getId()).toList();
        gtinRepository.deleteAllInBatch();
        syncDeletionEventService.recordDeletions("gtin", ids);
    }
}
