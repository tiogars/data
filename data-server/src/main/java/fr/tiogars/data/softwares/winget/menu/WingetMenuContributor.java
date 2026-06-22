package fr.tiogars.data.softwares.winget.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class WingetMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution("Données métier", "Applications Winget", "/winget/list", "desktop_windows", 18, true)
        );
    }
}
