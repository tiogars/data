package fr.tiogars.data.dev.docs.brick.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.docs.brick.entities.BrickEntity;

@Repository
public interface BrickRepository extends JpaRepository<BrickEntity, String> {

    Optional<BrickEntity> findByNumber(String number);

    List<BrickEntity> findAllByOrderByNumberAsc();
}
