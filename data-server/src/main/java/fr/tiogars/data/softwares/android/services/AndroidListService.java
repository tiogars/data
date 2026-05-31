package fr.tiogars.data.softwares.android.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.models.AndroidListResponse;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@Service
public class AndroidListService {

    private final AndroidRepository androidRepository;

    public AndroidListService(AndroidRepository androidRepository) {
        this.androidRepository = androidRepository;
    }

    public AndroidListResponse listAndroids() {
        List<Android> items = androidRepository.findAllByOrderByNameAsc().stream()
            .map(AndroidModelMapper::toModel)
            .toList();

        return new AndroidListResponse(items, items.size());
    }
}