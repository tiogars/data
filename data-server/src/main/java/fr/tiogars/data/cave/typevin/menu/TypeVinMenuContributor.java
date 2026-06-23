package fr.tiogars.data.cave.typevin.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class TypeVinMenuContributor implements MenuItemContributor {
    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(new MenuItemContribution("Cave", "Types de vin", "/type-vin/list", "local_drink", 12, true));
    }
}
