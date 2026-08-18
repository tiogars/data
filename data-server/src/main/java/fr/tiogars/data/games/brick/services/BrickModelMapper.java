package fr.tiogars.data.games.brick.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.tiogars.data.games.brick.entities.BrickEntity;
import fr.tiogars.data.games.brick.entities.ExternalLinkEntity;
import fr.tiogars.data.games.brick.models.Brick;
import fr.tiogars.data.games.brick.models.ExternalLink;

final class BrickModelMapper {

    private BrickModelMapper() {
    }

    static Brick toModel(BrickEntity entity) {
        Brick model = new Brick();
        model.setId(entity.getId());
        model.setNumber(entity.getNumber());
        model.setTitle(entity.getTitle());
        model.setTags(csvToTags(entity.getTags()));
        model.setImageBase64(entity.getImageBase64());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    static ExternalLink toModel(ExternalLinkEntity entity) {
        ExternalLink model = new ExternalLink();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setUrl(entity.getUrl());
        model.setEnabled(entity.isEnabled());
        return model;
    }

    static BrickEntity toEntity(Brick model) {
        BrickEntity entity = new BrickEntity();
        entity.setNumber(model.getNumber());
        entity.setTitle(model.getTitle());
        entity.setTags(tagsToCsv(model.getTags()));
        entity.setImageBase64(model.getImageBase64());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        return entity;
    }

    static ExternalLinkEntity toEntity(ExternalLink model) {
        ExternalLinkEntity entity = new ExternalLinkEntity();
        entity.setName(model.getName());
        entity.setUrl(model.getUrl());
        entity.setEnabled(model.isEnabled());
        return entity;
    }

    static List<String> csvToTags(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        return Arrays.stream(value.split(","))
            .map(BrickModelMapper::trim)
            .filter(item -> !item.isBlank())
            .toList();
    }

    static String tagsToCsv(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        return tags.stream()
            .map(BrickModelMapper::trim)
            .filter(item -> !item.isBlank())
            .collect(Collectors.joining(","));
    }

    private static String trim(@org.jspecify.annotations.NonNull String value) {
        return value.trim();
    }
}
