package fr.tiogars.data.cave.circonstance.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.circonstance.entities.CirconstanceEntity;

public interface CirconstanceRepository extends JpaRepository<CirconstanceEntity, String>, JpaSpecificationExecutor<CirconstanceEntity> {

    Optional<CirconstanceEntity> findByName(String name);

    List<CirconstanceEntity> findAllByOrderByNameAsc();
}
