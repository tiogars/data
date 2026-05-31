package fr.tiogars.data.softwares.android.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidGetOneService {

    private final AndroidRepository androidRepository;

    public AndroidGetOneService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public Android getAndroid(String id) {
        return androidRepository.findById(id)
            .map(AndroidModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Application Android introuvable."));
    }
}