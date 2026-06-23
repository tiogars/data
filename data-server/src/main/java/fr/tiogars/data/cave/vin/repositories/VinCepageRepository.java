package fr.tiogars.data.cave.vin.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.cave.vin.entities.VinCepageEntity;

public interface VinCepageRepository extends JpaRepository<VinCepageEntity, String> {

    List<VinCepageEntity> findByVinId(String vinId);

    List<VinCepageEntity> findByVinIdIn(Collection<String> vinIds);

    void deleteByVinId(String vinId);
}
