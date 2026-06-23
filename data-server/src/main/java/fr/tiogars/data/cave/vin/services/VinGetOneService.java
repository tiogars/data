package fr.tiogars.data.cave.vin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vin.entities.VinEntity;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.repositories.VinCepageRepository;
import fr.tiogars.data.cave.vin.repositories.VinCirconstanceRepository;
import fr.tiogars.data.cave.vin.repositories.VinRepository;
import fr.tiogars.data.cave.vin.repositories.VinVinTagRepository;
import fr.tiogars.data.common.exceptions.DataNotFoundException;

@Service
public class VinGetOneService {

    private final VinRepository vinRepository;
    private final VinCepageRepository vinCepageRepository;
    private final VinCirconstanceRepository vinCirconstanceRepository;
    private final VinVinTagRepository vinVinTagRepository;
    private final VinLookupHelper vinLookupHelper;

    public VinGetOneService(
        VinRepository vinRepository,
        VinCepageRepository vinCepageRepository,
        VinCirconstanceRepository vinCirconstanceRepository,
        VinVinTagRepository vinVinTagRepository,
        VinLookupHelper vinLookupHelper
    ) {
        this.vinRepository = vinRepository;
        this.vinCepageRepository = vinCepageRepository;
        this.vinCirconstanceRepository = vinCirconstanceRepository;
        this.vinVinTagRepository = vinVinTagRepository;
        this.vinLookupHelper = vinLookupHelper;
    }

    public Vin getVin(String id) {
        VinEntity entity = vinRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Vin non trouve pour l'id: " + id));
        var cepages = vinCepageRepository.findByVinId(id);
        var circonstances = vinCirconstanceRepository.findByVinId(id);
        var tags = vinVinTagRepository.findByVinId(id);
        VinResolutionContext context = vinLookupHelper.buildResolutionContext(
            java.util.List.of(entity),
            cepages,
            circonstances,
            tags
        );
        return VinModelMapper.toModel(entity, cepages, circonstances, tags, context);
    }
}
