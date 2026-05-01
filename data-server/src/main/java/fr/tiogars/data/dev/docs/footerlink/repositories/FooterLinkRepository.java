package fr.tiogars.data.dev.docs.footerlink.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.docs.footerlink.entities.FooterLinkEntity;

@Repository
public interface FooterLinkRepository extends JpaRepository<FooterLinkEntity, String> {

    Optional<FooterLinkEntity> findByLabel(String label);

    List<FooterLinkEntity> findAllByOrderByDisplayOrderAscLabelAsc();
}