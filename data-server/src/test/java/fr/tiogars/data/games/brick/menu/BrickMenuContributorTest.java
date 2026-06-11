package fr.tiogars.data.games.brick.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;

class BrickMenuContributorTest {

    private final BrickMenuContributor contributor = new BrickMenuContributor();

    @Test
    void shouldExposeBrickSettingsAndExternalLinksWhenRequestingContributions() {
        List<MenuItemContribution> contributions = contributor.getContributions();

        assertThat(contributions)
            .extracting(MenuItemContribution::label)
            .containsExactly("Bricks", "Settings", "External links");

        assertThat(contributions)
            .extracting(MenuItemContribution::parentLabel)
            .containsExactly("Games", "Bricks", "Settings");

        assertThat(contributions)
            .extracting(MenuItemContribution::path)
            .containsExactly("/brick/list", null, "/brick/settings/external-links");
    }
}
