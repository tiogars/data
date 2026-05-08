package fr.tiogars.data.dev.docs.brick.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.brick.entities.BrickEntity;
import fr.tiogars.data.dev.docs.brick.forms.BrickCreationForm;
import fr.tiogars.data.dev.docs.brick.models.Brick;
import fr.tiogars.data.dev.docs.brick.repositories.BrickRepository;

@Service
public class BrickCreationService {

    private final BrickRepository brickRepository;

    public BrickCreationService(BrickRepository brickRepository) {
        this.brickRepository = brickRepository;
    }

    public Brick createBrick(BrickCreationForm form) {
        validateUniqueNumber(form.getNumber(), null);

        BrickEntity entity = new BrickEntity();
        applyValues(entity, form.getNumber(), form.getTitle(), form.getTags(), form.getImageBase64());

        return BrickModelMapper.toModel(brickRepository.save(entity));
    }

    static void applyValues(BrickEntity entity, String number, String title, List<String> tags, String imageBase64) {
        entity.setNumber(requireText(number, "Le numero de brique est obligatoire."));
        entity.setTitle(requireText(title, "Le titre de la brique est obligatoire."));
        entity.setTags(BrickModelMapper.tagsToCsv(normalizeTags(tags)));
        entity.setImageBase64(normalizeNullableText(imageBase64));
    }

    void validateUniqueNumber(String number, String currentId) {
        brickRepository.findByNumber(requireText(number, "Le numero de brique est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> {
                throw new IllegalArgumentException("Une brique avec ce numero existe deja.");
            });
    }

    static List<String> normalizeTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        List<String> normalized = new ArrayList<>();

        for (String tag : tags) {
            if (tag == null || tag.isBlank()) {
                continue;
            }

            String value = tag.trim().toLowerCase();
            if (!normalized.contains(value)) {
                normalized.add(value);
            }
        }

        return normalized;
    }

    static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    static String normalizeNullableText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
