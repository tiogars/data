package fr.tiogars.data.products.gtin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;
import fr.tiogars.data.sync.services.SyncDeletionEventService;

@Service
public class GtinDeleteOneService {

    private final GtinRepository gtinRepository;
    private final SyncDeletionEventService syncDeletionEventService;

    public GtinDeleteOneService(GtinRepository gtinRepository, SyncDeletionEventService syncDeletionEventService) {
        this.gtinRepository = gtinRepository;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public void deleteGtin(String id) {
        if (!gtinRepository.existsById(id)) {
            throw new DataNotFoundException("GTIN non trouve pour l'id: " + id);
        }
        gtinRepository.deleteById(id);
        syncDeletionEventService.recordDeletion("gtin", id);
    }
}
