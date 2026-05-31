package fr.tiogars.data.products.gtin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinDeleteOneService {

    private final GtinRepository gtinRepository;

    public GtinDeleteOneService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public void deleteGtin(String id) {
        if (!gtinRepository.existsById(id)) {
            throw new DataNotFoundException("GTIN non trouve pour l'id: " + id);
        }
        gtinRepository.deleteById(id);
    }
}
