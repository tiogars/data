package fr.tiogars.data.cave.couleur.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class CouleurMenuContributor implements MenuItemContributor {
    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(new MenuItemContribution("Cave", "Couleurs", "/couleur/list", "palette", 11, true));
    }
}
