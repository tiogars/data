package fr.tiogars.data.locations.continent.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContributor;

@Component
public class ContinentMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution("Données métier", "Continents", "/continent", "public", 13, true)
        );
    }
}
