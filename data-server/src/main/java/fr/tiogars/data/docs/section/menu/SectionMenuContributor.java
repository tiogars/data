package fr.tiogars.data.docs.section.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class SectionMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution("Données métier", "Sections", "/section/list", "dataset", 11, true),
            new MenuItemContribution("Sections", "Paramètres docs", "/section/settings/docs", "folder_managed", 12, true)
        );
    }
}