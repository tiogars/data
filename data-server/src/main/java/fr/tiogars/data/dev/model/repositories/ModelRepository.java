package fr.tiogars.data.dev.model.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.dev.model.entities.ModelEntity;

public interface ModelRepository extends JpaRepository<ModelEntity, String>, JpaSpecificationExecutor<ModelEntity> {

    Optional<ModelEntity> findByName(String name);

    List<ModelEntity> findAllByOrderByNameAsc();
}
