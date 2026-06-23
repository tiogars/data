package fr.tiogars.data.cave.vin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.cave.vin.entities.VinPhotoEntity;

public interface VinPhotoRepository extends JpaRepository<VinPhotoEntity, String> {

    List<VinPhotoEntity> findByVinId(String vinId);

    void deleteByVinId(String vinId);
}
