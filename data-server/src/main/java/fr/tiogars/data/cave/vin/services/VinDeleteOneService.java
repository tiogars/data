package fr.tiogars.data.cave.vin.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.vin.repositories.VinCepageRepository;
import fr.tiogars.data.cave.vin.repositories.VinCirconstanceRepository;
import fr.tiogars.data.cave.vin.repositories.VinPhotoRepository;
import fr.tiogars.data.cave.vin.repositories.VinRepository;
import fr.tiogars.data.cave.vin.repositories.VinVinTagRepository;
import fr.tiogars.data.common.exceptions.DataNotFoundException;

@Service
public class VinDeleteOneService {

    private final VinRepository vinRepository;
    private final VinCepageRepository vinCepageRepository;
    private final VinCirconstanceRepository vinCirconstanceRepository;
    private final VinVinTagRepository vinVinTagRepository;
    private final VinPhotoRepository vinPhotoRepository;

    public VinDeleteOneService(
        VinRepository vinRepository,
        VinCepageRepository vinCepageRepository,
        VinCirconstanceRepository vinCirconstanceRepository,
        VinVinTagRepository vinVinTagRepository,
        VinPhotoRepository vinPhotoRepository
    ) {
        this.vinRepository = vinRepository;
        this.vinCepageRepository = vinCepageRepository;
        this.vinCirconstanceRepository = vinCirconstanceRepository;
        this.vinVinTagRepository = vinVinTagRepository;
        this.vinPhotoRepository = vinPhotoRepository;
    }

    @Transactional
    public void deleteVin(String id) {
        if (!vinRepository.existsById(id)) {
            throw new DataNotFoundException("Vin non trouve pour l'id: " + id);
        }
        vinPhotoRepository.deleteByVinId(id);
        vinCepageRepository.deleteByVinId(id);
        vinCirconstanceRepository.deleteByVinId(id);
        vinVinTagRepository.deleteByVinId(id);
        vinRepository.deleteById(id);
    }
}
