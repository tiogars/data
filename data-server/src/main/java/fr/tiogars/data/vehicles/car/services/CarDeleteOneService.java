package fr.tiogars.data.vehicles.car.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.sync.services.SyncDeletionEventService;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarDeleteOneService {

    private final CarRepository carRepository;
    private final SyncDeletionEventService syncDeletionEventService;

    public CarDeleteOneService(CarRepository carRepository, SyncDeletionEventService syncDeletionEventService) {
        this.carRepository = carRepository;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public void deleteCar(String id) {
        if (!carRepository.existsById(id)) {
            throw new DataNotFoundException("Voiture non trouvee pour l'id: " + id);
        }
        carRepository.deleteById(id);
        syncDeletionEventService.recordDeletion("car", id);
    }
}
