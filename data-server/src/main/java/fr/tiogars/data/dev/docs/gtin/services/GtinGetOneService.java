package fr.tiogars.data.dev.docs.gtin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.gtin.models.Gtin;
import fr.tiogars.data.dev.docs.gtin.repositories.GtinRepository;

@Service
public class GtinGetOneService {

    private final GtinRepository gtinRepository;

    public GtinGetOneService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public Gtin getGtin(String id) {
        return gtinRepository.findById(id)
            .map(GtinModelMapper::toModel)
            .orElseThrow(() -> new DataNotFoundException("GTIN non trouve pour l'id: " + id));
    }
}
