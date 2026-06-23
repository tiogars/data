package fr.tiogars.data.cave.vin.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.cave.vin.entities.VinCirconstanceEntity;

public interface VinCirconstanceRepository extends JpaRepository<VinCirconstanceEntity, String> {

    List<VinCirconstanceEntity> findByVinId(String vinId);

    List<VinCirconstanceEntity> findByVinIdIn(Collection<String> vinIds);

    void deleteByVinId(String vinId);
}
