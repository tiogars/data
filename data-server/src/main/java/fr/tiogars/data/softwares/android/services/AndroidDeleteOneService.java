package fr.tiogars.data.softwares.android.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidDeleteOneService {

    private final AndroidRepository androidRepository;

    public AndroidDeleteOneService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public void deleteAndroid(String id) {
        if (!androidRepository.existsById(id)) {
            throw new DataNotFoundException("Application Android introuvable.");
        }
        androidRepository.deleteById(id);
    }
}