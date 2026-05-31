package fr.tiogars.data.dev.githubrestconfig.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class GitHubRestConfigMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution(null, "Token GitHub REST", "/github-token-config", "key", 70, true)
        );
    }
}
