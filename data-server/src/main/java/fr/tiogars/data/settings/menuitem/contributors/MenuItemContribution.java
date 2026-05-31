package fr.tiogars.data.settings.menuitem.contributors;

public record MenuItemContribution(
    String parentLabel,
    String label,
    String path,
    String icon,
    int displayOrder,
    boolean defaultLoaded
) {
}
