package fr.tiogars.data.cave.contenant.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class ContenantMenuContributor implements MenuItemContributor { @Override public List<MenuItemContribution> getContributions() { return List.of(new MenuItemContribution("Cave", "Contenants", "/contenant/list", "local_bar", 13, true)); } }
