package fr.tiogars.data.cave.vin.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.vin.repositories.VinCepageRepository;
import fr.tiogars.data.cave.vin.repositories.VinCirconstanceRepository;
import fr.tiogars.data.cave.vin.repositories.VinPhotoRepository;
import fr.tiogars.data.cave.vin.repositories.VinRepository;
import fr.tiogars.data.cave.vin.repositories.VinVinTagRepository;

@Service
public class VinDeleteAllService {

    private final VinPhotoRepository vinPhotoRepository;
    private final VinCepageRepository vinCepageRepository;
    private final VinCirconstanceRepository vinCirconstanceRepository;
    private final VinVinTagRepository vinVinTagRepository;
    private final VinRepository vinRepository;

    public VinDeleteAllService(
        VinPhotoRepository vinPhotoRepository,
        VinCepageRepository vinCepageRepository,
        VinCirconstanceRepository vinCirconstanceRepository,
        VinVinTagRepository vinVinTagRepository,
        VinRepository vinRepository
    ) {
        this.vinPhotoRepository = vinPhotoRepository;
        this.vinCepageRepository = vinCepageRepository;
        this.vinCirconstanceRepository = vinCirconstanceRepository;
        this.vinVinTagRepository = vinVinTagRepository;
        this.vinRepository = vinRepository;
    }

    @Transactional
    public void deleteAllVins() {
        vinPhotoRepository.deleteAllInBatch();
        vinCepageRepository.deleteAllInBatch();
        vinCirconstanceRepository.deleteAllInBatch();
        vinVinTagRepository.deleteAllInBatch();
        vinRepository.deleteAllInBatch();
    }
}
