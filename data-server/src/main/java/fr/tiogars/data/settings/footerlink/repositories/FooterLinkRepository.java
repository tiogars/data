package fr.tiogars.data.settings.footerlink.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.settings.footerlink.entities.FooterLinkEntity;

public interface FooterLinkRepository extends JpaRepository<FooterLinkEntity, String>, JpaSpecificationExecutor<FooterLinkEntity> {

    Optional<FooterLinkEntity> findByLabel(String label);

    List<FooterLinkEntity> findAllByOrderByDisplayOrderAscLabelAsc();
}