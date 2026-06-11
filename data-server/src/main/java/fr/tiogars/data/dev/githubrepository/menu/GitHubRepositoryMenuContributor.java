package fr.tiogars.data.dev.githubrepository.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class GitHubRepositoryMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution(null, "Repositories GitHub", "/github-repository/search", "github", 60, true)
        );
    }
}
