package fr.tiogars.data.vehicles.car.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.sync.services.SyncDeletionEventService;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;

@Service
public class CarDeleteAllService {

    private final CarRepository carRepository;
    private final SyncDeletionEventService syncDeletionEventService;

    public CarDeleteAllService(CarRepository carRepository, SyncDeletionEventService syncDeletionEventService) {
        this.carRepository = carRepository;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public void deleteAllCars() {
        List<String> ids = carRepository.findAll().stream().map(entity -> entity.getId()).toList();
        carRepository.deleteAllInBatch();
        syncDeletionEventService.recordDeletions("car", ids);
    }
}
