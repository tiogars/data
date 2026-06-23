package fr.tiogars.data.cave.maison.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class MaisonMenuContributor implements MenuItemContributor { @Override public List<MenuItemContribution> getContributions() { return List.of(new MenuItemContribution("Cave", "Maisons", "/maison/list", "home", 16, true)); } }
