package fr.tiogars.data.cave.vintag.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;
import fr.tiogars.data.cave.vintag.models.VinTag;

@Service
public class VinTagGetOneService {
    private final VinTagRepository vinTagRepository;
    public VinTagGetOneService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    public VinTag getVinTag(String id) { return vinTagRepository.findById(id).map(VinTagModelMapper::toModel).orElseThrow(() -> new fr.tiogars.data.common.exceptions.DataNotFoundException("Tag de vin non trouve pour l'id: " + id)); }
}
