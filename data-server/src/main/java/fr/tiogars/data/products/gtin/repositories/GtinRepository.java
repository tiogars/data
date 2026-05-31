package fr.tiogars.data.products.gtin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.products.gtin.entities.GtinEntity;

@Repository
public interface GtinRepository extends JpaRepository<GtinEntity, String> {

    Optional<GtinEntity> findByCode(String code);

    List<GtinEntity> findAllByOrderByCodeAsc();
}
