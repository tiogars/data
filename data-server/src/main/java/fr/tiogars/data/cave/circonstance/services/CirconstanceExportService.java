package fr.tiogars.data.cave.circonstance.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;
import fr.tiogars.data.cave.circonstance.models.CirconstanceListResponse;

@Service
public class CirconstanceExportService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstanceExportService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    public CirconstanceListResponse exportCirconstances() { var items = circonstanceRepository.findAllByOrderByNameAsc().stream().map(CirconstanceModelMapper::toModel).toList(); return new CirconstanceListResponse(items, items.size()); }
}
