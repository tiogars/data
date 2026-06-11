package fr.tiogars.data.products.gtin.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.models.GtinListResponse;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@Service
public class GtinExportService {

    private final GtinRepository gtinRepository;

    public GtinExportService(GtinRepository gtinRepository) {
        this.gtinRepository = gtinRepository;
    }

    public GtinListResponse exportGtins() {
        List<Gtin> items = gtinRepository.findAllByOrderByCodeAsc().stream()
            .map(GtinModelMapper::toModel)
            .toList();

        return new GtinListResponse(items, items.size());
    }
}
