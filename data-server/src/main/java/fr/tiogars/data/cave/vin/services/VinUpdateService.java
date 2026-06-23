package fr.tiogars.data.cave.vin.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.vin.entities.VinEntity;
import fr.tiogars.data.cave.vin.forms.VinCreationForm;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.repositories.VinCepageRepository;
import fr.tiogars.data.cave.vin.repositories.VinCirconstanceRepository;
import fr.tiogars.data.cave.vin.repositories.VinRepository;
import fr.tiogars.data.cave.vin.repositories.VinVinTagRepository;
import fr.tiogars.data.common.exceptions.DataNotFoundException;

@Service
public class VinUpdateService {

    private final VinRepository vinRepository;
    private final VinCepageRepository vinCepageRepository;
    private final VinCirconstanceRepository vinCirconstanceRepository;
    private final VinVinTagRepository vinVinTagRepository;
    private final VinCreationService vinCreationService;
    private final VinGetOneService vinGetOneService;

    public VinUpdateService(
        VinRepository vinRepository,
        VinCepageRepository vinCepageRepository,
        VinCirconstanceRepository vinCirconstanceRepository,
        VinVinTagRepository vinVinTagRepository,
        VinCreationService vinCreationService,
        VinGetOneService vinGetOneService
    ) {
        this.vinRepository = vinRepository;
        this.vinCepageRepository = vinCepageRepository;
        this.vinCirconstanceRepository = vinCirconstanceRepository;
        this.vinVinTagRepository = vinVinTagRepository;
        this.vinCreationService = vinCreationService;
        this.vinGetOneService = vinGetOneService;
    }

    @Transactional
    public Vin updateVin(String id, VinCreationForm form) {
        VinEntity entity = vinRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Vin non trouve pour l'id: " + id));

        VinCreationService.applyValues(entity, form);
        vinRepository.save(entity);
        vinCepageRepository.deleteByVinId(id);
        vinCirconstanceRepository.deleteByVinId(id);
        vinVinTagRepository.deleteByVinId(id);
        vinCreationService.saveRelations(id, form);
        return vinGetOneService.getVin(id);
    }
}
