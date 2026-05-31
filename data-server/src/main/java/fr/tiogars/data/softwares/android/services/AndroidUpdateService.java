package fr.tiogars.data.softwares.android.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.softwares.android.entities.AndroidEntity;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidUpdateService {

    private final AndroidRepository androidRepository;

    public AndroidUpdateService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public Android updateAndroid(String id, Android android) {
        AndroidEntity entity = androidRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Application Android introuvable."));

        String packageName = AndroidCreationService.requireText(android != null ? android.getPackageName() : null, "Le package Android est obligatoire.");
        androidRepository.findByPackageName(packageName)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new IllegalArgumentException("Une application Android avec ce package existe deja.");
            });

        AndroidCreationService.applyValues(
            entity,
            android != null ? android.getName() : null,
            packageName,
            android != null ? android.getCategory() : null,
            android != null ? android.getDescription() : null,
            android != null ? android.getIcon() : null
        );

        return AndroidModelMapper.toModel(androidRepository.save(entity));
    }
}