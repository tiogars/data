package fr.tiogars.data.softwares.winget.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.softwares.winget.entities.WingetEntity;

public interface WingetRepository extends JpaRepository<WingetEntity, String>, JpaSpecificationExecutor<WingetEntity> {

    Optional<WingetEntity> findByWingetId(String wingetId);

    List<WingetEntity> findAllByOrderByNameAsc();
}
