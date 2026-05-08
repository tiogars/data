package fr.tiogars.data.dev.docs.brick.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.docs.brick.entities.ExternalLinkEntity;

@Repository
public interface ExternalLinkRepository extends JpaRepository<ExternalLinkEntity, String> {

    Optional<ExternalLinkEntity> findByNameIgnoreCase(String name);

    List<ExternalLinkEntity> findAllByOrderByNameAsc();
}
