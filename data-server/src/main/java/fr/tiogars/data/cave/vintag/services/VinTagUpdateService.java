package fr.tiogars.data.cave.vintag.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.vintag.entities.VinTagEntity;
import fr.tiogars.data.cave.vintag.models.VinTag;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;

@Service
public class VinTagUpdateService {
    private final VinTagRepository vinTagRepository;
    private final VinTagCreationService vinTagCreationService;
    public VinTagUpdateService(VinTagRepository vinTagRepository, VinTagCreationService vinTagCreationService) { this.vinTagRepository = vinTagRepository; this.vinTagCreationService = vinTagCreationService; }
    public VinTag updateVinTag(String id, VinTag vinTag) {
        VinTagEntity entity = vinTagRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Tag de vin non trouve pour l'id: " + id));
        vinTagCreationService.validateUniqueName(vinTag.getName(), id);
        VinTagCreationService.applyValues(entity, vinTag.getName());
        return VinTagModelMapper.toModel(vinTagRepository.save(entity));
    }
}
