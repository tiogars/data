package fr.tiogars.data.cave.appellation.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class AppellationMenuContributor implements MenuItemContributor {
    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(new MenuItemContribution("Cave", "Appellations", "/appellation/list", "wine_bar", 10, true));
    }
}
