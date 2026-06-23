package fr.tiogars.data.cave.circonstance.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class CirconstanceMenuContributor implements MenuItemContributor {
    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(new MenuItemContribution("Cave", "Circonstances", "/circonstance/list", "event", 14, true));
    }
}
