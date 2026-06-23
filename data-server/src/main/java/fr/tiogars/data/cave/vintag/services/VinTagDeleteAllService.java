package fr.tiogars.data.cave.vintag.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;

@Service
public class VinTagDeleteAllService {
    private final VinTagRepository vinTagRepository;
    public VinTagDeleteAllService(VinTagRepository vinTagRepository) { this.vinTagRepository = vinTagRepository; }
    public void deleteAllVinTags() { vinTagRepository.deleteAllInBatch(); }
}
