package fr.tiogars.data.dev.docs.menuitem.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, MenuItemEntity> savedItems = new HashMap<>();
        
        // Créer les menus de niveau supérieur (parents)
        MenuItemEntity businessGroup = createMenuItem("Données métier", null, "storage", 10, true);
        MenuItemEntity interfaceGroup = createMenuItem("Interface", null, "palette", 20, true);
        MenuItemEntity configGroup = createMenuItem("Configuration", null, "build", 30, true);
        MenuItemEntity accountGroup = createMenuItem("Compte", null, "account_circle", 40, true);
        
        // Sauvegarder les groupes
        List<MenuItemEntity> groups = List.of(businessGroup, interfaceGroup, configGroup, accountGroup);
        
        for (MenuItemEntity group : groups) {
            if (menuItemRepository.findByLabel(group.getLabel()).isEmpty()) {
                MenuItemEntity saved = menuItemRepository.save(group);
                savedItems.put(group.getLabel(), saved);
            } else {
                savedItems.put(group.getLabel(), menuItemRepository.findByLabel(group.getLabel()).get());
            }
        }
        
        // Créer les menus enfants
        List<MenuItemEntity> businessItems = List.of(
            createMenuItemWithParent("Bricks", "/brick", "toys", 11, true, savedItems.get("Données métier")),
            createMenuItemWithParent("Modeles", "/model", "schema", 12, true, savedItems.get("Données métier")),
            createMenuItemWithParent("Continents", "/continent", "public", 13, true, savedItems.get("Données métier")),
            createMenuItemWithParent("Gestion URLs", "/url-manager", "link", 14, true, savedItems.get("Données métier"))
        );
        
        List<MenuItemEntity> interfaceItems = List.of(
            createMenuItemWithParent("Cartes accueil", "/url-cards", "dashboard_customize", 21, true, savedItems.get("Interface"))
        );
        
        List<MenuItemEntity> configItems = List.of(
            createMenuItemWithParent("Gateway API", "/gateway-config", "settings_ethernet", 31, true, savedItems.get("Configuration")),
            createMenuItemWithParent("Authentification", "/auth-config", "security", 32, true, savedItems.get("Configuration")),
            createMenuItemWithParent("Entites JPA", "/server-info/jpa-entities", "account_tree", 33, true, savedItems.get("Configuration")),
            createMenuItemWithParent("Gestion menu", "/menu-item", "menu", 34, true, savedItems.get("Configuration"))
        );
        
        List<MenuItemEntity> accountItems = List.of(
            createMenuItemWithParent("Mon compte", "/auth/account", "manage_accounts", 41, true, savedItems.get("Compte"))
        );
        
        List<MenuItemEntity> allChildren = List.of(
            businessItems, interfaceItems, configItems, accountItems
        ).stream().flatMap(List::stream).toList();
        
        // Sauvegarder les enfants
        for (MenuItemEntity child : allChildren) {
            if (menuItemRepository.findByLabel(child.getLabel()).isEmpty()) {
                menuItemRepository.save(child);
            }
        }
        
        // Ajouter les anciens menus (pour migration)
        List<MenuItemEntity> legacyItems = List.of(
            createMenuItem("Liens footer", "/footer-link", "link", 50, true),
            createMenuItem("Repositories GitHub", "/github-repository", "github", 60, true),
            createMenuItem("Token GitHub REST", "/github-token-config", "key", 70, true),
            createMenuItem("Version Java serveur", "/server-info/java-version", "memory", 80, true),
            createMenuItem("GTIN", "/gtin", "menu", 90, true),
            createMenuItem("Marques", "/brand", "menu", 100, true)
        );
        
        for (MenuItemEntity item : legacyItems) {
            if (menuItemRepository.findByLabel(item.getLabel()).isEmpty()) {
                menuItemRepository.save(item);
            }
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

    private MenuItemEntity createMenuItemWithParent(String label, String path, String icon, int displayOrder, boolean defaultLoaded, MenuItemEntity parent) {
        MenuItemEntity entity = createMenuItem(label, path, icon, displayOrder, defaultLoaded);
        entity.setParent(parent);
        return entity;
    }
}
