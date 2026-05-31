package fr.tiogars.data.products.brand.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.products.brand.entities.BrandEntity;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, String> {

    Optional<BrandEntity> findByName(String name);

    List<BrandEntity> findAllByOrderByNameAsc();
}
