package fr.tiogars.data.softwares.winget.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.softwares.winget.entities.WingetEntity;
import fr.tiogars.data.softwares.winget.forms.WingetUpdateForm;
import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.repositories.WingetRepository;

@Service
public class WingetUpdateService {

    private final WingetRepository wingetRepository;

    public WingetUpdateService(WingetRepository wingetRepository) {
        this.wingetRepository = wingetRepository;
    }

    public Winget updateWinget(String id, WingetUpdateForm form) {
        WingetEntity entity = wingetRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Application Winget introuvable."));

        String wingetId = WingetCreationService.requireText(form.getWingetId(), "L'identifiant Winget est obligatoire.");
        wingetRepository.findByWingetId(wingetId)
            .filter(other -> !other.getId().equals(id))
            .ifPresent(other -> {
                throw new IllegalArgumentException("Une application avec cet identifiant Winget existe deja.");
            });

        WingetCreationService.applyValues(
            entity,
            form.getName(),
            form.getDescription(),
            wingetId,
            form.getInstallCommand(),
            form.getTags());

        return WingetModelMapper.toModel(wingetRepository.save(entity));
    }
}
