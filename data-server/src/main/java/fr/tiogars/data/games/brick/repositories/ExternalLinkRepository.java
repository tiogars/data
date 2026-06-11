package fr.tiogars.data.games.brick.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.games.brick.entities.ExternalLinkEntity;

public interface ExternalLinkRepository extends JpaRepository<ExternalLinkEntity, String> {

    Optional<ExternalLinkEntity> findByNameIgnoreCase(String name);

    List<ExternalLinkEntity> findAllByOrderByNameAsc();
}
