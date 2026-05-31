package fr.tiogars.data.dev.model.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.model.entities.ModelEntity;

@Repository
public interface ModelRepository extends JpaRepository<ModelEntity, String> {

    Optional<ModelEntity> findByName(String name);

    List<ModelEntity> findAllByOrderByNameAsc();
}
