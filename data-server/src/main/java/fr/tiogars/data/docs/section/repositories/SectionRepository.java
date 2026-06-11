package fr.tiogars.data.docs.section.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.docs.section.entities.SectionEntity;

public interface SectionRepository extends JpaRepository<SectionEntity, String>, JpaSpecificationExecutor<SectionEntity> {

    Optional<SectionEntity> findByName(String name);

}
