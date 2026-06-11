package fr.tiogars.data.vehicles.car.menu;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.menuitem.contributors.MenuItemContribution;
import fr.tiogars.data.settings.menuitem.contributors.MenuItemContributor;

@Component
public class CarMenuContributor implements MenuItemContributor {

    @Override
    public List<MenuItemContribution> getContributions() {
        return List.of(
            new MenuItemContribution("Données métier", "Voitures", null, "directions_car", 18, true),
            new MenuItemContribution("Voitures", "Dashboard", "/car/dashboard", "monitoring", 19, true),
            new MenuItemContribution("Voitures", "Liste voitures", "/car/list", "directions_car", 20, true),
            new MenuItemContribution("Voitures", "Creation", "/car/create", "add_circle", 21, true),
            new MenuItemContribution("Voitures", "Saisie tableau", "/car-mileage/table", "table_view", 22, true),
            new MenuItemContribution("Voitures", "Saisie formulaire", "/car-mileage/form", "edit_note", 23, true)
        );
    }
}
