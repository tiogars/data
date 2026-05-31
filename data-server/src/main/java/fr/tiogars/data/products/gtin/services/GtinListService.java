package fr.tiogars.data.products.gtin.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.GtinListResponse;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

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
