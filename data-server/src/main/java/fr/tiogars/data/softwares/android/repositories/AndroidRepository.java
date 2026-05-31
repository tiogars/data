package fr.tiogars.data.softwares.android.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.softwares.android.entities.AndroidEntity;

public interface AndroidRepository extends JpaRepository<AndroidEntity, String> {

    List<AndroidEntity> findAllByOrderByNameAsc();

    Optional<AndroidEntity> findByPackageName(String packageName);

    boolean existsByPackageName(String packageName);
}