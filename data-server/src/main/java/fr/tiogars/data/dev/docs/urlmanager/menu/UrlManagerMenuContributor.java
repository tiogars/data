package fr.tiogars.data.dev.docs.urlmanager.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContributor;

@Component
public class UrlManagerMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution("Données métier", "Gestion URLs", "/url-manager", "link", 14, true)
        );
    }
}
