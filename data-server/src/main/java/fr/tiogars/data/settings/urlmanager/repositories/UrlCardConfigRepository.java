package fr.tiogars.data.settings.urlmanager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.settings.urlmanager.entities.UrlCardConfigEntity;

public interface UrlCardConfigRepository extends JpaRepository<UrlCardConfigEntity, String> {

    List<UrlCardConfigEntity> findAllByOrderByTitleAsc();
}
