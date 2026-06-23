package fr.tiogars.data.cave.vintag.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;
import fr.tiogars.data.cave.vintag.models.VinTagListResponse;

@Service
public class VinTagExportService {
    private final VinTagRepository vinTagRepository;
    public VinTagExportService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    public VinTagListResponse exportVinTags() { var items = vinTagRepository.findAllByOrderByNameAsc().stream().map(VinTagModelMapper::toModel).toList(); return new VinTagListResponse(items, items.size()); }
}
