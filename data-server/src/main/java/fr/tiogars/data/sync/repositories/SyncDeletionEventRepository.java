package fr.tiogars.data.sync.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.sync.entities.SyncDeletionEventEntity;

public interface SyncDeletionEventRepository extends JpaRepository<SyncDeletionEventEntity, String> {

    List<SyncDeletionEventEntity> findAllByDomainAndDeletedAtGreaterThanAndDeletedAtLessThanEqualOrderByDeletedAtAscResourceIdAsc(
        String domain,
        Instant deletedAfter,
        Instant deletedBefore
    );
}
