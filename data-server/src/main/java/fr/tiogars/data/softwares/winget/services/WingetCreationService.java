package fr.tiogars.data.softwares.winget.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.winget.entities.WingetEntity;
import fr.tiogars.data.softwares.winget.forms.WingetCreationForm;
import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.repositories.WingetRepository;

@Service
public class WingetCreationService {

    private final WingetRepository wingetRepository;

    public WingetCreationService(WingetRepository wingetRepository) {
        this.wingetRepository = wingetRepository;
    }

    public Winget createWinget(WingetCreationForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Les donnees Winget sont obligatoires.");
        }

        String wingetId = requireText(form.getWingetId(), "L'identifiant Winget est obligatoire.");
        if (wingetRepository.findByWingetId(wingetId).isPresent()) {
            throw new IllegalArgumentException("Une application avec cet identifiant Winget existe deja.");
        }

        WingetEntity entity = new WingetEntity();
        applyValues(entity, form.getName(), form.getDescription(), wingetId, form.getInstallCommand(), form.getTags());
        return WingetModelMapper.toModel(wingetRepository.save(entity));
    }

    public static void applyValues(WingetEntity entity, String name, String description, String wingetId, String installCommand, List<String> tags) {
        entity.setName(requireText(name, "Le nom de l'application est obligatoire."));
        entity.setDescription(normalizeNullableText(description));
        entity.setWingetId(requireText(wingetId, "L'identifiant Winget est obligatoire."));
        entity.setInstallCommand(requireText(installCommand, "La commande Winget est obligatoire."));
        entity.setTags(normalizeTags(tags));
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

    public static List<String> normalizeTags(List<String> tags) {
        if (tags == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(tags.stream()
            .filter(value -> value != null && !value.isBlank())
            .map(String::trim)
            .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
    }
}
