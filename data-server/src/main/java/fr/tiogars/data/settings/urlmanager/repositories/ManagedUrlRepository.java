package fr.tiogars.data.settings.urlmanager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.settings.urlmanager.entities.ManagedUrlEntity;

public interface ManagedUrlRepository extends JpaRepository<ManagedUrlEntity, String> {

    List<ManagedUrlEntity> findAllByOrderByLabelAsc();
}
