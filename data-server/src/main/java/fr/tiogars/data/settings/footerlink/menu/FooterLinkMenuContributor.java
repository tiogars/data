package fr.tiogars.data.settings.footerlink.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContributor;

@Component
public class FooterLinkMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution(null, "Liens footer", "/footer-link", "link", 50, true)
        );
    }
}
