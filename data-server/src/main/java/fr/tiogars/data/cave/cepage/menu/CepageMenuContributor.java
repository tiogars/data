package fr.tiogars.data.cave.cepage.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class CepageMenuContributor implements MenuItemContributor {
    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(new MenuItemContribution("Cave", "Cépages", "/cepage/list", "grass", 15, true));
    }
}
