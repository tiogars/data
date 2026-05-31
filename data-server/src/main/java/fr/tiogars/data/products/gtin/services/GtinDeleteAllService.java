package fr.tiogars.data.products.gtin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinDeleteAllService {

    private final GtinRepository gtinRepository;

    public GtinDeleteAllService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public void deleteAllGtins() {
        gtinRepository.deleteAllInBatch();
    }
}
