package fr.tiogars.data.cave.vintag.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.vintag.entities.VinTagEntity;

public interface VinTagRepository extends JpaRepository<VinTagEntity, String>, JpaSpecificationExecutor<VinTagEntity> {

    Optional<VinTagEntity> findByName(String name);

    List<VinTagEntity> findAllByOrderByNameAsc();
}
