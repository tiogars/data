package fr.tiogars.data.cave.contenant.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.contenant.entities.ContenantEntity;

public interface ContenantRepository extends JpaRepository<ContenantEntity, String>, JpaSpecificationExecutor<ContenantEntity> {

    Optional<ContenantEntity> findByName(String name);

    List<ContenantEntity> findAllByOrderByNameAsc();
}
