package fr.tiogars.data.games.brick.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.jspecify.annotations.NonNull;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;

class BrickMenuContributorTest {

    private final BrickMenuContributor contributor = new BrickMenuContributor();

    @Test
    void shouldExposeBrickSettingsAndExternalLinksWhenRequestingContributions() {
        List<MenuItemContribution> contributions = contributor.getContributions();

        assertThat(contributions)
            .extracting(BrickMenuContributorTest::getLabel)
            .containsExactly("Bricks", "Settings", "External links");

        assertThat(contributions)
            .extracting(BrickMenuContributorTest::getParentLabel)
            .containsExactly("Games", "Bricks", "Settings");

        assertThat(contributions)
            .extracting(BrickMenuContributorTest::getPath)
            .containsExactly("/brick/list", null, "/brick/settings/external-links");
    }

    private static String getLabel(@NonNull MenuItemContribution item) { return item.label(); }
    private static String getParentLabel(@NonNull MenuItemContribution item) { return item.parentLabel(); }
    private static String getPath(@NonNull MenuItemContribution item) { return item.path(); }
}
