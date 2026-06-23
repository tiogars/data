package fr.tiogars.data.cave.couleur.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.couleur.entities.CouleurEntity;

public interface CouleurRepository extends JpaRepository<CouleurEntity, String>, JpaSpecificationExecutor<CouleurEntity> {

    Optional<CouleurEntity> findByName(String name);

    List<CouleurEntity> findAllByOrderByNameAsc();
}
