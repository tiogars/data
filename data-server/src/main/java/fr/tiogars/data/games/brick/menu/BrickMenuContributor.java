package fr.tiogars.data.games.brick.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class BrickMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution("Games", "Bricks", "/brick/list", "smart_toy", 11, true),
            new MenuItemContribution("Bricks", "Settings", null, "settings", 12, true),
            new MenuItemContribution("Settings", "External links", "/brick/settings/external-links", "link", 13, true)
        );
    }
}
