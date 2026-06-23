package fr.tiogars.data.cave.circonstance.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;
import fr.tiogars.data.cave.circonstance.models.CirconstanceListResponse;

@Service
public class CirconstanceListService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstanceListService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    public CirconstanceListResponse listCirconstances() { var entities = circonstanceRepository.findAllByOrderByNameAsc(); return new CirconstanceListResponse(entities.stream().map(CirconstanceModelMapper::toModel).toList(), entities.size()); }
}
