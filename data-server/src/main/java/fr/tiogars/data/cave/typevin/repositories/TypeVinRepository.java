package fr.tiogars.data.cave.typevin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.cave.typevin.entities.TypeVinEntity;

public interface TypeVinRepository extends JpaRepository<TypeVinEntity, String>, JpaSpecificationExecutor<TypeVinEntity> {

    Optional<TypeVinEntity> findByName(String name);

    List<TypeVinEntity> findAllByOrderByNameAsc();
}
