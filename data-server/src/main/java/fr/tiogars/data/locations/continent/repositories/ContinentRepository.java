package fr.tiogars.data.locations.continent.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.locations.continent.entities.ContinentEntity;

/**
 * Repository pour la gestion des continents.
 */
public interface ContinentRepository extends JpaRepository<ContinentEntity, String> {

    Optional<ContinentEntity> findByCode(String code);

    Optional<ContinentEntity> findByName(String name);

    List<ContinentEntity> findAllByOrderByNameAsc();
}
