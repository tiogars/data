package fr.tiogars.data.cave.cepage.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;
import fr.tiogars.data.cave.cepage.models.CepageListResponse;

@Service
public class CepageExportService {
    private final CepageRepository cepageRepository;
    public CepageExportService(CepageRepository cepageRepository) { this.cepageRepository = cepageRepository; }
    public CepageListResponse exportCepages() { var items = cepageRepository.findAllByOrderByNameAsc().stream().map(CepageModelMapper::toModel).toList(); return new CepageListResponse(items, items.size()); }
}
