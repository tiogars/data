package fr.tiogars.data.products.brand.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.products.brand.entities.BrandEntity;

public interface BrandRepository extends JpaRepository<BrandEntity, String>, JpaSpecificationExecutor<BrandEntity> {

    Optional<BrandEntity> findByName(String name);

    List<BrandEntity> findAllByOrderByNameAsc();
}
