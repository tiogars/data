package fr.tiogars.data.softwares.android.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.android.entities.AndroidEntity;
import fr.tiogars.data.softwares.android.forms.AndroidCreationForm;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidCreationService {

    private final AndroidRepository androidRepository;

    public AndroidCreationService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public Android createAndroid(AndroidCreationForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Les donnees Android sont obligatoires.");
        }

        String name = requireText(form.getName(), "Le nom de l'application Android est obligatoire.");
        String packageName = requireText(form.getPackageName(), "Le package Android est obligatoire.");

        if (androidRepository.existsByPackageName(packageName)) {
            throw new IllegalArgumentException("Une application Android avec ce package existe deja.");
        }

        AndroidEntity entity = new AndroidEntity();
        applyValues(entity, name, packageName, form.getCategory(), form.getDescription(), form.getIcon());
        return AndroidModelMapper.toModel(androidRepository.save(entity));
    }

    public static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    public static String normalizeNullableText(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    public static List<String> normalizeCategories(List<String> categories) {
        if (categories == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(categories.stream()
            .filter(value -> value != null && !value.isBlank())
            .map(String::trim)
            .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
    }

    public static void applyValues(AndroidEntity entity, String name, String packageName, List<String> category, String description, String icon) {
        entity.setName(requireText(name, "Le nom de l'application Android est obligatoire."));
        entity.setPackageName(requireText(packageName, "Le package Android est obligatoire."));
        entity.setCategory(normalizeCategories(category));
        entity.setDescription(normalizeNullableText(description));
        entity.setIcon(normalizeNullableText(icon));
    }
}