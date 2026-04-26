package fr.tiogars.data.dev.docs.section.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.docs.section.entities.SectionEntity;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, String> {

    Optional<SectionEntity> findByName(String name);

}
