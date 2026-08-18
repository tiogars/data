package fr.tiogars.data.softwares.winget.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.softwares.winget.entities.WingetEntity;
import fr.tiogars.data.softwares.winget.forms.WingetImportForm;
import fr.tiogars.data.softwares.winget.models.WingetImportResponse;
import fr.tiogars.data.softwares.winget.repositories.WingetRepository;

@Service
public class WingetImportService {

    private final WingetRepository wingetRepository;

    public WingetImportService(WingetRepository wingetRepository) {
        this.wingetRepository = wingetRepository;
    }

    @Transactional
    public WingetImportResponse importWingets(WingetImportForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Les donnees d'import Winget sont obligatoires.");
        }

        List<String> wingetIds = parseWingetIdsText(form.getWingetIdsText());
        if (wingetIds.isEmpty()) {
            throw new IllegalArgumentException("Au moins un identifiant Winget est requis pour l'import.");
        }

        WingetImportResponse response = new WingetImportResponse();
        Set<String> seenWingetIds = new HashSet<>();

        for (String wingetId : wingetIds) {
            if (seenWingetIds.contains(wingetId) || wingetRepository.findByWingetId(wingetId).isPresent()) {
                response.setSkippedCount(response.getSkippedCount() + 1);
                response.getSkippedWingetIds().add(wingetId);
                continue;
            }

            seenWingetIds.add(wingetId);

            WingetEntity entity = new WingetEntity();
            String name = deriveNameFromWingetId(wingetId);
            String installCommand = buildInstallCommand(wingetId);

            WingetCreationService.applyValues(entity, name, null, wingetId, installCommand, List.of());
            response.getCreatedItems().add(WingetModelMapper.toModel(wingetRepository.save(entity)));
            response.setCreatedCount(response.getCreatedCount() + 1);
        }

        return response;
    }

    static List<String> parseWingetIdsText(String wingetIdsText) {
        if (wingetIdsText == null) {
            return new ArrayList<>();
        }

        return wingetIdsText
            .lines()
                .map(WingetImportService::trim)
            .filter(value -> !value.isEmpty())
            .toList();
    }

            private static String trim(@NonNull String value) {
            return value.trim();
            }

    static String buildInstallCommand(String wingetId) {
        return "winget install -e --id " + WingetCreationService.requireText(wingetId, "L'identifiant Winget est obligatoire.");
    }

    static String deriveNameFromWingetId(String wingetId) {
        String normalizedWingetId = WingetCreationService.requireText(wingetId, "L'identifiant Winget est obligatoire.");
        int firstDotIndex = normalizedWingetId.indexOf('.');

        if (firstDotIndex < 0 || firstDotIndex == normalizedWingetId.length() - 1) {
            return normalizedWingetId;
        }

        String afterFirstDot = normalizedWingetId.substring(firstDotIndex + 1).trim();
        return afterFirstDot.isEmpty() ? normalizedWingetId : afterFirstDot;
    }
}
