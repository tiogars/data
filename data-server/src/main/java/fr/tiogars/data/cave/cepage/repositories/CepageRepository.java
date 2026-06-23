package fr.tiogars.data.cave.cepage.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.cepage.entities.CepageEntity;

public interface CepageRepository extends JpaRepository<CepageEntity, String>, JpaSpecificationExecutor<CepageEntity> {

    Optional<CepageEntity> findByName(String name);

    List<CepageEntity> findAllByOrderByNameAsc();
}
