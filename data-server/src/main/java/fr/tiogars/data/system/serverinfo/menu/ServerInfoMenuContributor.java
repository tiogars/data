package fr.tiogars.data.system.serverinfo.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class ServerInfoMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution("Configuration", "Entites JPA", "/server-info/jpa-entities", "account_tree", 33, true),
            new MenuItemContribution(null, "Version Java serveur", "/server-info/java-version", "memory", 80, true)
        );
    }
}
