package fr.tiogars.data.cave.vin.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.cave.vin.entities.VinVinTagEntity;

public interface VinVinTagRepository extends JpaRepository<VinVinTagEntity, String> {

    List<VinVinTagEntity> findByVinId(String vinId);

    List<VinVinTagEntity> findByVinIdIn(Collection<String> vinIds);

    void deleteByVinId(String vinId);
}
