package fr.tiogars.data.settings.urlmanager.services;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.tiogars.data.settings.urlmanager.entities.ManagedUrlEntity;
import fr.tiogars.data.settings.urlmanager.entities.UrlCardConfigEntity;
import fr.tiogars.data.settings.urlmanager.models.ManagedUrl;
import fr.tiogars.data.settings.urlmanager.models.UrlCardConfig;

final class UrlManagerModelMapper {

    private UrlManagerModelMapper() {
    }

    static ManagedUrl toModel(ManagedUrlEntity entity) {
        ManagedUrl model = new ManagedUrl();
        model.setId(entity.getId());
        model.setLabel(entity.getLabel());
        model.setUrl(entity.getUrl());
        model.setTags(csvToTags(entity.getTags()));
        model.setDescription(entity.getDescription());
        return model;
    }

    static UrlCardConfig toModel(UrlCardConfigEntity entity) {
        UrlCardConfig model = new UrlCardConfig();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setTags(csvToTags(entity.getTags()));
        model.setMatchMode(entity.getMatchMode());
        return model;
    }

    static ManagedUrlEntity toEntity(ManagedUrl model) {
        ManagedUrlEntity entity = new ManagedUrlEntity();
        entity.setLabel(model.getLabel());
        entity.setUrl(model.getUrl());
        entity.setTags(tagsToCsv(model.getTags()));
        entity.setDescription(model.getDescription());
        return entity;
    }

    static UrlCardConfigEntity toEntity(UrlCardConfig model) {
        UrlCardConfigEntity entity = new UrlCardConfigEntity();
        entity.setTitle(model.getTitle());
        entity.setTags(tagsToCsv(model.getTags()));
        entity.setMatchMode(model.getMatchMode());
        return entity;
    }

    private static List<String> csvToTags(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        return Arrays.stream(value.split(","))
            .map(tag -> Objects.requireNonNull(tag).trim())
            .filter(item -> !item.isBlank())
            .toList();
    }

    private static String tagsToCsv(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        return tags.stream()
            .map(tag -> Objects.requireNonNull(tag).trim())
            .filter(item -> !item.isBlank())
            .collect(Collectors.joining(","));
    }
}
