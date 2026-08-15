package fr.tiogars.data.products.gtin.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.tiogars.data.products.gtin.entities.GtinEntity;

public interface GtinRepository extends JpaRepository<GtinEntity, String>, JpaSpecificationExecutor<GtinEntity> {

    Optional<GtinEntity> findByCode(String code);

    List<GtinEntity> findAllByOrderByCodeAsc();

    /**
     * Retourne une page de synchronisation triee par (updatedAt, id) via une pagination keyset.
     */
    @Query("""
        SELECT g FROM GtinEntity g
        WHERE g.updatedAt > :updatedAfter
          AND g.updatedAt <= :windowEnd
          AND (g.updatedAt > :lastUpdatedAt OR (g.updatedAt = :lastUpdatedAt AND g.id > :lastId))
        ORDER BY g.updatedAt ASC, g.id ASC
        """)
    List<GtinEntity> findSyncPage(
        @Param("updatedAfter") Instant updatedAfter,
        @Param("windowEnd") Instant windowEnd,
        @Param("lastUpdatedAt") Instant lastUpdatedAt,
        @Param("lastId") String lastId,
        Pageable pageable
    );
}
