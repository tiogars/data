package fr.tiogars.data.cave.vin.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.vin.entities.VinCepageEntity;
import fr.tiogars.data.cave.vin.entities.VinCirconstanceEntity;
import fr.tiogars.data.cave.vin.entities.VinEntity;
import fr.tiogars.data.cave.vin.entities.VinVinTagEntity;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.models.VinListResponse;
import fr.tiogars.data.cave.vin.repositories.VinCepageRepository;
import fr.tiogars.data.cave.vin.repositories.VinCirconstanceRepository;
import fr.tiogars.data.cave.vin.repositories.VinRepository;
import fr.tiogars.data.cave.vin.repositories.VinVinTagRepository;

@Service
public class VinListService {

    private final VinRepository vinRepository;
    private final VinCepageRepository vinCepageRepository;
    private final VinCirconstanceRepository vinCirconstanceRepository;
    private final VinVinTagRepository vinVinTagRepository;
    private final VinLookupHelper vinLookupHelper;

    public VinListService(
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

    public VinListResponse listVins() {
        List<VinEntity> entities = vinRepository.findAllByOrderByCreatedAtDesc();
        List<Vin> items = mapEntities(entities);
        return new VinListResponse(items, items.size());
    }

    List<Vin> mapEntities(List<VinEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        Set<String> vinIds = entities.stream().map(VinListService::getVinId).collect(Collectors.toSet());
        List<VinCepageEntity> cepages = vinCepageRepository.findByVinIdIn(vinIds);
        List<VinCirconstanceEntity> circonstances = vinCirconstanceRepository.findByVinIdIn(vinIds);
        List<VinVinTagEntity> tags = vinVinTagRepository.findByVinIdIn(vinIds);

        Map<String, List<VinCepageEntity>> cepagesByVinId = cepages.stream()
            .collect(Collectors.groupingBy(VinListService::getVinCepageId));
        Map<String, List<VinCirconstanceEntity>> circonstancesByVinId = circonstances.stream()
            .collect(Collectors.groupingBy(VinListService::getVinCirconstanceId));
        Map<String, List<VinVinTagEntity>> tagsByVinId = tags.stream()
            .collect(Collectors.groupingBy(VinListService::getVinVinTagId));
        VinResolutionContext context = vinLookupHelper.buildResolutionContext(entities, cepages, circonstances, tags);

        return entities.stream()
            .map(entity -> VinModelMapper.toModel(
                entity,
                cepagesByVinId.getOrDefault(entity.getId(), List.of()),
                circonstancesByVinId.getOrDefault(entity.getId(), List.of()),
                tagsByVinId.getOrDefault(entity.getId(), List.of()),
                context
            ))
            .toList();
    }

    private static String getVinId(@NonNull VinEntity entity) {
        return entity.getId();
    }

    private static String getVinCepageId(@NonNull VinCepageEntity entity) {
        return entity.getVinId();
    }

    private static String getVinCirconstanceId(@NonNull VinCirconstanceEntity entity) {
        return entity.getVinId();
    }

    private static String getVinVinTagId(@NonNull VinVinTagEntity entity) {
        return entity.getVinId();
    }
}
