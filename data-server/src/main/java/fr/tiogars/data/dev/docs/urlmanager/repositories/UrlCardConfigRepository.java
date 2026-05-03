package fr.tiogars.data.dev.docs.urlmanager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.docs.urlmanager.entities.UrlCardConfigEntity;

@Repository
public interface UrlCardConfigRepository extends JpaRepository<UrlCardConfigEntity, String> {

    List<UrlCardConfigEntity> findAllByOrderByTitleAsc();
}
