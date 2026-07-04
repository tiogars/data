package fr.tiogars.data.docs.section.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Sort;

import fr.tiogars.data.docs.section.entities.SectionEntity;

public interface SectionRepository extends JpaRepository<SectionEntity, String>, JpaSpecificationExecutor<SectionEntity> {

    Sort DEFAULT_SECTION_SORT = Sort.by(
        Sort.Order.asc("displayOrder"),
        Sort.Order.asc("name")
    );

    Optional<SectionEntity> findByName(String name);

    Optional<SectionEntity> findByNameAndDocument_Id(String name, String documentId);

    List<SectionEntity> findAllByDocument_Id(String documentId, Sort sort);

    boolean existsByIdAndDocument_Id(String id, String documentId);

}
