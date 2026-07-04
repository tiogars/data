package fr.tiogars.data.docs.sectiondocument.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;

public interface SectionDocumentRepository extends JpaRepository<SectionDocumentEntity, String> {

    Optional<SectionDocumentEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
