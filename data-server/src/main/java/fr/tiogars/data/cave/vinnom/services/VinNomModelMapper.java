package fr.tiogars.data.cave.vinnom.services;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.entities.VinNomEntity;
import fr.tiogars.data.cave.vinnom.models.VinNom;

final class VinNomModelMapper {
    private VinNomModelMapper() { }
    static VinNom toModel(VinNomEntity entity, Map<String, String> maisonNamesById) {
        VinNom model = new VinNom();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setMaisonId(entity.getMaisonId());
        model.setMaisonName(entity.getMaisonId() == null ? null : maisonNamesById.get(entity.getMaisonId()));
        return model;
    }
    static VinNom toModel(VinNomEntity entity, MaisonRepository maisonRepository) {
        return toModel(entity, resolveMaisonNames(List.of(entity), maisonRepository));
    }
    static List<VinNom> toModels(List<VinNomEntity> entities, MaisonRepository maisonRepository) {
        Map<String, String> maisonNamesById = resolveMaisonNames(entities, maisonRepository);
        return entities.stream().map(entity -> toModel(entity, maisonNamesById)).toList();
    }
    private static Map<String, String> resolveMaisonNames(Collection<VinNomEntity> entities, MaisonRepository maisonRepository) {
        Set<String> maisonIds = entities.stream().map(VinNomEntity::getMaisonId).filter(id -> id != null && !id.isBlank()).collect(Collectors.toCollection(LinkedHashSet::new));
        if (maisonIds.isEmpty()) return Map.of();
        return maisonRepository.findAllById(maisonIds).stream().collect(Collectors.toMap(entity -> entity.getId(), entity -> entity.getName()));
    }
}
