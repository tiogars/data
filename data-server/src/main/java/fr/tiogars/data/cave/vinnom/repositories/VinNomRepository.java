package fr.tiogars.data.cave.vinnom.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.vinnom.entities.VinNomEntity;

public interface VinNomRepository extends JpaRepository<VinNomEntity, String>, JpaSpecificationExecutor<VinNomEntity> {
    Optional<VinNomEntity> findByNameAndMaisonId(String name, String maisonId);
    Optional<VinNomEntity> findByNameAndMaisonIdIsNull(String name);
    List<VinNomEntity> findAllByOrderByNameAsc();
}
