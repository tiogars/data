package fr.tiogars.data.vehicles.carmileage.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;

public interface CarMileageRepository extends JpaRepository<CarMileageEntity, String>, JpaSpecificationExecutor<CarMileageEntity> {

    List<CarMileageEntity> findByCar_IdOrderByReadingAtAsc(String carId);

    /**
     * Retourne une page de synchronisation triee par (updatedAt, id) via une pagination keyset.
     */
    @Query("""
        SELECT m FROM CarMileageEntity m
        JOIN FETCH m.car
        WHERE m.updatedAt > :updatedAfter
          AND m.updatedAt <= :windowEnd
          AND (m.updatedAt > :lastUpdatedAt OR (m.updatedAt = :lastUpdatedAt AND m.id > :lastId))
        ORDER BY m.updatedAt ASC, m.id ASC
        """)
    List<CarMileageEntity> findSyncPage(
        @Param("updatedAfter") Instant updatedAfter,
        @Param("windowEnd") Instant windowEnd,
        @Param("lastUpdatedAt") Instant lastUpdatedAt,
        @Param("lastId") String lastId,
        Pageable pageable
    );
}
