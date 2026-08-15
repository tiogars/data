package fr.tiogars.data.vehicles.car.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.tiogars.data.vehicles.car.entities.CarEntity;

public interface CarRepository extends JpaRepository<CarEntity, String>, JpaSpecificationExecutor<CarEntity> {

    Optional<CarEntity> findByName(String name);

    Optional<CarEntity> findByVehicleRegistrationPlate(String vehicleRegistrationPlate);

    List<CarEntity> findAllByOrderByNameAsc();

    /**
     * Retourne une page de synchronisation triee par (updatedAt, id) via une pagination keyset.
     */
    @Query("""
        SELECT c FROM CarEntity c
        WHERE c.updatedAt > :updatedAfter
          AND c.updatedAt <= :windowEnd
          AND (c.updatedAt > :lastUpdatedAt OR (c.updatedAt = :lastUpdatedAt AND c.id > :lastId))
        ORDER BY c.updatedAt ASC, c.id ASC
        """)
    List<CarEntity> findSyncPage(
        @Param("updatedAfter") Instant updatedAfter,
        @Param("windowEnd") Instant windowEnd,
        @Param("lastUpdatedAt") Instant lastUpdatedAt,
        @Param("lastId") String lastId,
        Pageable pageable
    );
}
