package fr.tiogars.data.dev.docs.gtin.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.gtin.entities.GtinEntity;
import fr.tiogars.data.dev.docs.gtin.models.GtinListResponse;
import fr.tiogars.data.dev.docs.gtin.repositories.GtinRepository;

@Service
public class GtinListService {

    private final GtinRepository gtinRepository;

    public GtinListService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public GtinListResponse listGtins() {
        List<GtinEntity> entities = gtinRepository.findAllByOrderByCodeAsc();
        return new GtinListResponse(entities.stream().map(GtinModelMapper::toModel).toList(), entities.size());
    }
}
