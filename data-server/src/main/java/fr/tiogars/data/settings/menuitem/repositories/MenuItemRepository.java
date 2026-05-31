package fr.tiogars.data.settings.menuitem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.settings.menuitem.entities.MenuItemEntity;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, String> {

    Optional<MenuItemEntity> findByLabel(String label);

    Optional<MenuItemEntity> findByPath(String path);

    List<MenuItemEntity> findAllByOrderByDisplayOrderAscLabelAsc();
}
