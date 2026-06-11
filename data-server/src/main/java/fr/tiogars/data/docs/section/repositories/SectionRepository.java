package fr.tiogars.data.docs.section.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.docs.section.entities.SectionEntity;

public interface SectionRepository extends JpaRepository<SectionEntity, String> {

    Optional<SectionEntity> findByName(String name);

}
