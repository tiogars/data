package fr.tiogars.data.products.gtin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.products.gtin.entities.GtinEntity;

public interface GtinRepository extends JpaRepository<GtinEntity, String>, JpaSpecificationExecutor<GtinEntity> {

    Optional<GtinEntity> findByCode(String code);

    List<GtinEntity> findAllByOrderByCodeAsc();
}
