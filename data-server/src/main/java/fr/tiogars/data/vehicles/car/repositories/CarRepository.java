package fr.tiogars.data.vehicles.car.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.data.vehicles.car.entities.CarEntity;

public interface CarRepository extends JpaRepository<CarEntity, String>, JpaSpecificationExecutor<CarEntity> {

    Optional<CarEntity> findByName(String name);

    List<CarEntity> findAllByOrderByNameAsc();
}
