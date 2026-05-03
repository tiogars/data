package fr.tiogars.data.dev.docs.gtin.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.gtin.repositories.GtinRepository;

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
