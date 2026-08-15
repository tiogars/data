package fr.tiogars.data.softwares.android.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.tiogars.data.softwares.android.entities.AndroidEntity;

public interface AndroidRepository extends JpaRepository<AndroidEntity, String>, JpaSpecificationExecutor<AndroidEntity> {

    List<AndroidEntity> findAllByOrderByNameAsc();

    Optional<AndroidEntity> findByPackageName(String packageName);

    boolean existsByPackageName(String packageName);

    /**
     * Retourne une page de synchronisation triee par (updatedAt, id) via une pagination keyset.
     */
    @Query("""
        SELECT a FROM AndroidEntity a
        WHERE a.updatedAt > :updatedAfter
          AND a.updatedAt <= :windowEnd
          AND (a.updatedAt > :lastUpdatedAt OR (a.updatedAt = :lastUpdatedAt AND a.id > :lastId))
        ORDER BY a.updatedAt ASC, a.id ASC
        """)
    List<AndroidEntity> findSyncPage(
        @Param("updatedAfter") Instant updatedAfter,
        @Param("windowEnd") Instant windowEnd,
        @Param("lastUpdatedAt") Instant lastUpdatedAt,
        @Param("lastId") String lastId,
        Pageable pageable
    );
}