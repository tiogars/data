package fr.tiogars.data.cave.vintag.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;
import fr.tiogars.data.cave.vintag.models.VinTagListResponse;

@Service
public class VinTagListService {
    private final VinTagRepository vinTagRepository;
    public VinTagListService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    public VinTagListResponse listVinTags() { var entities = vinTagRepository.findAllByOrderByNameAsc(); return new VinTagListResponse(entities.stream().map(VinTagModelMapper::toModel).toList(), entities.size()); }
}
