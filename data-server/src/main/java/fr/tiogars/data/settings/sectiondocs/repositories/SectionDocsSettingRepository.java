package fr.tiogars.data.settings.sectiondocs.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.settings.sectiondocs.entities.SectionDocsSettingEntity;

public interface SectionDocsSettingRepository extends JpaRepository<SectionDocsSettingEntity, String> {

    Optional<SectionDocsSettingEntity> findBySectionId(String sectionId);

    boolean existsBySectionId(String sectionId);
}