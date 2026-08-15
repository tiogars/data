package fr.tiogars.data.softwares.winget.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.tiogars.data.softwares.winget.entities.WingetEntity;

public interface WingetRepository extends JpaRepository<WingetEntity, String>, JpaSpecificationExecutor<WingetEntity> {

    Optional<WingetEntity> findByWingetId(String wingetId);

    List<WingetEntity> findAllByOrderByNameAsc();

    /**
     * Retourne une page de synchronisation triee par (updatedAt, id) via une pagination keyset.
     */
    @Query("""
        SELECT w FROM WingetEntity w
        WHERE w.updatedAt > :updatedAfter
          AND w.updatedAt <= :windowEnd
          AND (w.updatedAt > :lastUpdatedAt OR (w.updatedAt = :lastUpdatedAt AND w.id > :lastId))
        ORDER BY w.updatedAt ASC, w.id ASC
        """)
    List<WingetEntity> findSyncPage(
        @Param("updatedAfter") Instant updatedAfter,
        @Param("windowEnd") Instant windowEnd,
        @Param("lastUpdatedAt") Instant lastUpdatedAt,
        @Param("lastId") String lastId,
        Pageable pageable
    );
}
