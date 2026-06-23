package fr.tiogars.data.cave.appellation.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.appellation.entities.AppellationEntity;

public interface AppellationRepository extends JpaRepository<AppellationEntity, String>, JpaSpecificationExecutor<AppellationEntity> {

    Optional<AppellationEntity> findByName(String name);

    List<AppellationEntity> findAllByOrderByNameAsc();
}
