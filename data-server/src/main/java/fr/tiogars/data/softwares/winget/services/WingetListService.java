package fr.tiogars.data.softwares.winget.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.models.WingetListResponse;
import fr.tiogars.data.softwares.winget.repositories.WingetRepository;

@Service
public class WingetListService {

    private final WingetRepository wingetRepository;

    public WingetListService(WingetRepository wingetRepository) {
        this.wingetRepository = wingetRepository;
    }

    public WingetListResponse listWingets() {
        List<Winget> items = wingetRepository.findAllByOrderByNameAsc().stream()
            .map(WingetModelMapper::toModel)
            .toList();
        return new WingetListResponse(items, items.size());
    }
}
