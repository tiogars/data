package fr.tiogars.data.cave.vin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.vin.entities.VinEntity;

public interface VinRepository extends JpaRepository<VinEntity, String>, JpaSpecificationExecutor<VinEntity> {

    List<VinEntity> findAllByOrderByCreatedAtDesc();
}
