package fr.tiogars.data.dev.docs.menuitem.contributors;

public record MenuItemContribution(
    String parentLabel,
    String label,
    String path,
    String icon,
    int displayOrder,
    boolean defaultLoaded
) {
}
