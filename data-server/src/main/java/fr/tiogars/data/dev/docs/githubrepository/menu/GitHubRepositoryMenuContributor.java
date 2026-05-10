package fr.tiogars.data.dev.docs.githubrepository.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.dev.docs.menuitem.contributors.MenuItemContributor;

@Component
public class GitHubRepositoryMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution(null, "Repositories GitHub", "/github-repository", "github", 60, true)
        );
    }
}
