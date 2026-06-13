package fr.tiogars.data.sync.services;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.sync.entities.SyncDeletionEventEntity;
import fr.tiogars.data.sync.repositories.SyncDeletionEventRepository;

@Service
public class SyncDeletionEventService {

    private final SyncDeletionEventRepository syncDeletionEventRepository;

    public SyncDeletionEventService(SyncDeletionEventRepository syncDeletionEventRepository) {
        this.syncDeletionEventRepository = syncDeletionEventRepository;
    }

    public void recordDeletion(String domain, String resourceId) {
        if (resourceId == null || resourceId.isBlank()) {
            return;
        }

        SyncDeletionEventEntity event = new SyncDeletionEventEntity();
        event.setDomain(domain);
        event.setResourceId(resourceId);
        event.setDeletedAt(Instant.now());
        syncDeletionEventRepository.save(event);
    }

    public void recordDeletions(String domain, Collection<String> resourceIds) {
        if (resourceIds == null || resourceIds.isEmpty()) {
            return;
        }

        Instant now = Instant.now();
        List<SyncDeletionEventEntity> events = resourceIds.stream()
            .filter(id -> id != null && !id.isBlank())
            .map(id -> {
                SyncDeletionEventEntity event = new SyncDeletionEventEntity();
                event.setDomain(domain);
                event.setResourceId(id);
                event.setDeletedAt(now);
                return event;
            })
            .toList();

        if (!events.isEmpty()) {
            syncDeletionEventRepository.saveAll(events);
        }
    }

    public List<String> findDeletedIds(String domain, Instant deletedAfter, Instant deletedBefore) {
        if (deletedAfter == null || deletedBefore == null) {
            return List.of();
        }

        if (!deletedAfter.isBefore(deletedBefore)) {
            return List.of();
        }

        return syncDeletionEventRepository
            .findAllByDomainAndDeletedAtGreaterThanAndDeletedAtLessThanEqualOrderByDeletedAtAscResourceIdAsc(
                domain,
                deletedAfter,
                deletedBefore
            )
            .stream()
            .map(SyncDeletionEventEntity::getResourceId)
            .toList();
    }
}
