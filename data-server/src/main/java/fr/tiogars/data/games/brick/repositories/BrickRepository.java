package fr.tiogars.data.games.brick.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.data.games.brick.entities.BrickEntity;

public interface BrickRepository extends JpaRepository<BrickEntity, String> {

    Optional<BrickEntity> findByNumber(String number);

    List<BrickEntity> findAllByOrderByNumberAsc();
}
