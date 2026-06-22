package fr.tiogars.data.softwares.winget.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.repositories.WingetRepository;

@Service
public class WingetGetOneService {

    private final WingetRepository wingetRepository;

    public WingetGetOneService(WingetRepository wingetRepository) {
        this.wingetRepository = wingetRepository;
    }

    public Winget getWinget(String id) {
        return wingetRepository.findById(id)
            .map(WingetModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("Application Winget introuvable."));
    }
}
