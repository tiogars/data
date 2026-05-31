package fr.tiogars.data.products.gtin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinUpdateService {

    private final GtinRepository gtinRepository;
    private final GtinCreationService gtinCreationService;

    public GtinUpdateService(GtinRepository gtinRepository, GtinCreationService gtinCreationService) {
        this.gtinRepository = gtinRepository;
        this.gtinCreationService = gtinCreationService;
    }

    public Gtin updateGtin(String id, Gtin gtinUpdate) {
        GtinEntity entity = gtinRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("GTIN non trouve pour l'id: " + id));

        gtinCreationService.validateUniqueCode(gtinUpdate.getCode(), id);
        GtinCreationService.applyValues(entity, gtinUpdate.getCode(), gtinUpdate.getDescription());

        return GtinModelMapper.toModel(gtinRepository.save(entity));
    }
}
