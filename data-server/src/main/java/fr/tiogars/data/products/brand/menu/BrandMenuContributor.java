package fr.tiogars.data.products.brand.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContributor;

@Component
public class BrandMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution("Données métier", "Marques", "/brand", "label", 15, true)
        );
    }
}
