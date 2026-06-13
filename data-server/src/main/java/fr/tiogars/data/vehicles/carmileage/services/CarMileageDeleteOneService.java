package fr.tiogars.data.vehicles.carmileage.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.sync.services.SyncDeletionEventService;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class CarMileageDeleteOneService {

    private final CarMileageRepository carMileageRepository;
    private final SyncDeletionEventService syncDeletionEventService;

    public CarMileageDeleteOneService(CarMileageRepository carMileageRepository, SyncDeletionEventService syncDeletionEventService) {
        this.carMileageRepository = carMileageRepository;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public void deleteCarMileage(String id) {
        if (!carMileageRepository.existsById(id)) {
            throw new DataNotFoundException("Releve non trouve pour l'id: " + id);
        }
        carMileageRepository.deleteById(id);
        syncDeletionEventService.recordDeletion("car-mileage", id);
    }
}
