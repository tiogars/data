package fr.tiogars.data.softwares.android.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidDeleteAllService {

    private final AndroidRepository androidRepository;

    public AndroidDeleteAllService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public void deleteAllAndroids() {
        androidRepository.deleteAllInBatch();
    }
}