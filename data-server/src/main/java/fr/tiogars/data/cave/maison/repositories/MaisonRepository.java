package fr.tiogars.data.cave.maison.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.maison.entities.MaisonEntity;

public interface MaisonRepository extends JpaRepository<MaisonEntity, String>, JpaSpecificationExecutor<MaisonEntity> {

    Optional<MaisonEntity> findByName(String name);

    List<MaisonEntity> findAllByOrderByNameAsc();
}
