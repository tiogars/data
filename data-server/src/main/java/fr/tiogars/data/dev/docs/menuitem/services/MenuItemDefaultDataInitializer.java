package fr.tiogars.data.dev.docs.menuitem.services;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.tiogars.data.dev.docs.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.dev.docs.menuitem.repositories.MenuItemRepository;

@Component
public class MenuItemDefaultDataInitializer implements CommandLineRunner {

    private final MenuItemRepository menuItemRepository;

    public MenuItemDefaultDataInitializer(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) {
        List<MenuItemEntity> defaults = List.of(
            createMenuItem("Sections", "/section", "inbox", 10, true),
            createMenuItem("Liens footer", "/footer-link", "link", 20, true),
            createMenuItem("Repositories GitHub", "/github-repository", "github", 30, true),
            createMenuItem("Token GitHub REST", "/github-token-config", "key", 40, true),
            createMenuItem("Gestion menu", "/menu-item", "menu", 50, true),
            createMenuItem("Version Java serveur", "/server-info/java-version", "memory", 60, true),
            createMenuItem("GTIN", "/gtin", "menu", 70, true)
        );

        List<MenuItemEntity> missingDefaults = defaults.stream()
            .filter(item -> menuItemRepository.findByPath(item.getPath()).isEmpty())
            .toList();

        if (!missingDefaults.isEmpty()) {
            menuItemRepository.saveAll(missingDefaults);
        }
    }

    private MenuItemEntity createMenuItem(String label, String path, String icon, int displayOrder, boolean defaultLoaded) {
        MenuItemEntity entity = new MenuItemEntity();
        entity.setLabel(label);
        entity.setPath(path);
        entity.setIcon(icon);
        entity.setDisplayOrder(displayOrder);
        entity.setDefaultLoaded(defaultLoaded);
        return entity;
    }
}
