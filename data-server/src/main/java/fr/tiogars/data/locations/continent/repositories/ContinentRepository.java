package fr.tiogars.data.locations.continent.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.locations.continent.entities.ContinentEntity;

/**
 * Repository pour la gestion des continents.
 */
public interface ContinentRepository extends JpaRepository<ContinentEntity, String>, JpaSpecificationExecutor<ContinentEntity> {

    Optional<ContinentEntity> findByCode(String code);

    Optional<ContinentEntity> findByName(String name);

    List<ContinentEntity> findAllByOrderByNameAsc();
}
