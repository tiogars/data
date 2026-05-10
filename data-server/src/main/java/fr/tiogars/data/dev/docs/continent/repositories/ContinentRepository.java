package fr.tiogars.data.dev.docs.continent.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.docs.continent.entities.ContinentEntity;

/**
 * Repository pour la gestion des continents.
 */
@Repository
public interface ContinentRepository extends JpaRepository<ContinentEntity, String> {

    Optional<ContinentEntity> findByCode(String code);

    Optional<ContinentEntity> findByName(String name);

    List<ContinentEntity> findAllByOrderByNameAsc();
}
