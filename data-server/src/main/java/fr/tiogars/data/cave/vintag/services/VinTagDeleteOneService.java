package fr.tiogars.data.cave.vintag.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;

@Service
public class VinTagDeleteOneService {
    private final VinTagRepository vinTagRepository;
    public VinTagDeleteOneService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    public void deleteVinTag(String id) { if (!vinTagRepository.existsById(id)) throw new fr.tiogars.data.common.exceptions.DataNotFoundException("Tag de vin non trouve pour l'id: " + id); vinTagRepository.deleteById(id); }
}
