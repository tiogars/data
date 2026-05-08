package fr.tiogars.data.dev.docs.brick.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.dev.docs.brick.entities.BrickEntity;
import fr.tiogars.data.dev.docs.brick.entities.ExternalLinkEntity;
import fr.tiogars.data.dev.docs.brick.models.Brick;
import fr.tiogars.data.dev.docs.brick.models.BrickState;
import fr.tiogars.data.dev.docs.brick.models.ExternalLink;
import fr.tiogars.data.dev.docs.brick.repositories.BrickRepository;
import fr.tiogars.data.dev.docs.brick.repositories.ExternalLinkRepository;

@Service
public class BrickImportExportService {

    private final BrickRepository brickRepository;
    private final ExternalLinkRepository externalLinkRepository;

    public BrickImportExportService(BrickRepository brickRepository, ExternalLinkRepository externalLinkRepository) {
        this.brickRepository = brickRepository;
        this.externalLinkRepository = externalLinkRepository;
    }

    public BrickState exportState() {
        List<Brick> bricks = brickRepository.findAllByOrderByNumberAsc().stream()
            .map(BrickModelMapper::toModel)
            .toList();

        List<ExternalLink> externalLinks = externalLinkRepository.findAllByOrderByNameAsc().stream()
            .map(BrickModelMapper::toModel)
            .toList();

        return new BrickState(bricks, collectTags(bricks), externalLinks);
    }

    @Transactional
    public BrickState importState(List<Brick> rawBricks, List<ExternalLink> rawExternalLinks) {
        List<Brick> bricks = rawBricks != null ? rawBricks : List.of();
        List<ExternalLink> externalLinks = rawExternalLinks != null ? rawExternalLinks : List.of();

        List<Brick> uniqueBricks = deduplicateBricks(bricks);
        List<ExternalLink> uniqueExternalLinks = deduplicateExternalLinks(externalLinks);

        brickRepository.deleteAllInBatch();
        externalLinkRepository.deleteAllInBatch();

        List<BrickEntity> brickEntities = uniqueBricks.stream()
            .map(item -> {
                BrickEntity entity = BrickModelMapper.toEntity(item);
                BrickCreationService.applyValues(
                    entity,
                    item.getNumber(),
                    item.getTitle(),
                    item.getTags(),
                    item.getImageBase64()
                );
                return entity;
            })
            .toList();

        List<ExternalLinkEntity> externalLinkEntities = uniqueExternalLinks.stream()
            .map(item -> {
                ExternalLinkEntity entity = BrickModelMapper.toEntity(item);
                ExternalLinkCreationService.applyValues(entity, item.getName(), item.getUrl(), item.isEnabled());
                return entity;
            })
            .toList();

        brickRepository.saveAll(brickEntities);
        externalLinkRepository.saveAll(externalLinkEntities);

        return exportState();
    }

    private static List<Brick> deduplicateBricks(List<Brick> items) {
        List<Brick> unique = new ArrayList<>();
        Set<String> seenNumbers = new TreeSet<>();

        for (Brick item : items) {
            if (item == null) {
                continue;
            }

            String number = BrickCreationService.requireText(item.getNumber(), "Le numero de brique est obligatoire.");
            if (!seenNumbers.add(number)) {
                continue;
            }

            item.setNumber(number);
            item.setTitle(BrickCreationService.requireText(item.getTitle(), "Le titre de la brique est obligatoire."));
            item.setTags(BrickCreationService.normalizeTags(item.getTags()));
            item.setImageBase64(BrickCreationService.normalizeNullableText(item.getImageBase64()));
            unique.add(item);
        }

        return unique;
    }

    private static List<ExternalLink> deduplicateExternalLinks(List<ExternalLink> items) {
        List<ExternalLink> unique = new ArrayList<>();
        Set<String> seenNames = new TreeSet<>();

        for (ExternalLink item : items) {
            if (item == null) {
                continue;
            }

            String name = ExternalLinkCreationService.requireText(item.getName(), "Le nom du lien externe est obligatoire.");
            String normalizedKey = name.toLowerCase();

            if (!seenNames.add(normalizedKey)) {
                continue;
            }

            item.setName(name);
            item.setUrl(ExternalLinkCreationService.requireText(item.getUrl(), "L'URL du lien externe est obligatoire."));
            unique.add(item);
        }

        return unique;
    }

    private static List<String> collectTags(List<Brick> bricks) {
        Set<String> tags = new TreeSet<>();

        for (Brick brick : bricks) {
            tags.addAll(BrickCreationService.normalizeTags(brick.getTags()));
        }

        return new ArrayList<>(tags);
    }
}
