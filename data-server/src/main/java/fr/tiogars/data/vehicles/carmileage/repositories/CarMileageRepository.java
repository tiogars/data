package fr.tiogars.data.vehicles.carmileage.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;

public interface CarMileageRepository extends JpaRepository<CarMileageEntity, String>, JpaSpecificationExecutor<CarMileageEntity> {

    List<CarMileageEntity> findByCar_IdOrderByReadingAtAsc(String carId);
}
