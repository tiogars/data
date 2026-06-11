package fr.tiogars.data.softwares.android.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.softwares.android.entities.AndroidEntity;

public interface AndroidRepository extends JpaRepository<AndroidEntity, String>, JpaSpecificationExecutor<AndroidEntity> {

    List<AndroidEntity> findAllByOrderByNameAsc();

    Optional<AndroidEntity> findByPackageName(String packageName);

    boolean existsByPackageName(String packageName);
}