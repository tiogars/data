package fr.tiogars.data.dev.docs.urlmanager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.docs.urlmanager.entities.ManagedUrlEntity;

@Repository
public interface ManagedUrlRepository extends JpaRepository<ManagedUrlEntity, String> {

    List<ManagedUrlEntity> findAllByOrderByLabelAsc();
}
