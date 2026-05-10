package fr.tiogars.data.dev.docs.menuitem.services;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MenuGroupDefinitions {

    public List<MenuGroupDefinition> groups() {
        return List.of(
            new MenuGroupDefinition("Games", "sports_esports", 5),
            new MenuGroupDefinition("Données métier", "storage", 10),
            new MenuGroupDefinition("Interface", "palette", 20),
            new MenuGroupDefinition("Configuration", "build", 30),
            new MenuGroupDefinition("Compte", "account_circle", 40)
        );
    }

    public record MenuGroupDefinition(String label, String icon, int displayOrder) {
    }
}
