package fr.tiogars.data.settings.menuitem.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;
import fr.tiogars.data.settings.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.settings.menuitem.repositories.MenuItemRepository;

@Component
public class MenuItemDefaultDataInitializer implements CommandLineRunner {

    private final MenuItemRepository menuItemRepository;
    private final MenuGroupDefinitions menuGroupDefinitions;
    private final List<MenuItemContributor> menuItemContributors;

    public MenuItemDefaultDataInitializer(
        MenuItemRepository menuItemRepository,
        MenuGroupDefinitions menuGroupDefinitions,
        List<MenuItemContributor> menuItemContributors
    ) {
        this.menuItemRepository = menuItemRepository;
        this.menuGroupDefinitions = menuGroupDefinitions;
        this.menuItemContributors = menuItemContributors;
    }

    @Override
    public void run(String... args) {
        Map<String, MenuItemEntity> savedItemsByLabel = new HashMap<>();

        for (MenuGroupDefinitions.MenuGroupDefinition group : menuGroupDefinitions.groups()) {
            MenuItemEntity upserted = upsertMenuItem(
                new MenuItemContribution(
                    null,
                    group.label(),
                    null,
                    group.icon(),
                    group.displayOrder(),
                    true),
                savedItemsByLabel
            );
            savedItemsByLabel.put(group.label(), upserted);
        }

        for (MenuItemContributor contributor : menuItemContributors) {
            for (MenuItemContribution contribution : contributor.getContributions()) {
                upsertMenuItem(contribution, savedItemsByLabel);
            }
        }

        for (MenuItemContribution contribution : getSpecialCaseContributions()) {
            upsertMenuItem(contribution, savedItemsByLabel);
        }
    }

    private List<MenuItemContribution> getSpecialCaseContributions() {
        return List.of(
            new MenuItemContribution("Interface", "Cartes accueil", "/url-cards", "dashboard_customize", 21, true),
            new MenuItemContribution("Configuration", "Gateway API", "/gateway-config", "settings_ethernet", 31, true),
            new MenuItemContribution("Configuration", "Authentification", "/auth-config", "security", 32, true),
            new MenuItemContribution("Configuration", "Gestion menu", "/menu-item/list", "menu", 34, true),
            new MenuItemContribution("Compte", "Mon compte", "/auth/account", "manage_accounts", 41, true)
        );
    }

    private MenuItemEntity upsertMenuItem(MenuItemContribution contribution, Map<String, MenuItemEntity> cacheByLabel) {
        MenuItemEntity entity = menuItemRepository.findByLabel(contribution.label())
            .orElseGet(MenuItemEntity::new);

        entity.setLabel(contribution.label());
        entity.setPath(contribution.path());
        entity.setIcon(contribution.icon());
        entity.setDisplayOrder(contribution.displayOrder());
        entity.setDefaultLoaded(contribution.defaultLoaded());

        if (contribution.parentLabel() == null) {
            entity.setParent(null);
        } else {
            MenuItemEntity parent = cacheByLabel.computeIfAbsent(
                contribution.parentLabel(),
                label -> menuItemRepository.findByLabel(label)
                    .orElseThrow(() -> new IllegalArgumentException("Groupe parent introuvable: " + label))
            );
            entity.setParent(parent);
        }

        MenuItemEntity saved = menuItemRepository.save(entity);
        cacheByLabel.put(saved.getLabel(), saved);
        return saved;
    }
}
