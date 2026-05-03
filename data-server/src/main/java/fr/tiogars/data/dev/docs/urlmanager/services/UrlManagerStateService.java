package fr.tiogars.data.dev.docs.urlmanager.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.dev.docs.urlmanager.entities.ManagedUrlEntity;
import fr.tiogars.data.dev.docs.urlmanager.entities.UrlCardConfigEntity;
import fr.tiogars.data.dev.docs.urlmanager.models.ManagedUrl;
import fr.tiogars.data.dev.docs.urlmanager.models.UrlCardConfig;
import fr.tiogars.data.dev.docs.urlmanager.models.UrlManagerState;
import fr.tiogars.data.dev.docs.urlmanager.repositories.ManagedUrlRepository;
import fr.tiogars.data.dev.docs.urlmanager.repositories.UrlCardConfigRepository;

@Service
public class UrlManagerStateService {

    private final ManagedUrlRepository managedUrlRepository;
    private final UrlCardConfigRepository urlCardConfigRepository;

    public UrlManagerStateService(
        ManagedUrlRepository managedUrlRepository,
        UrlCardConfigRepository urlCardConfigRepository
    ) {
        this.managedUrlRepository = managedUrlRepository;
        this.urlCardConfigRepository = urlCardConfigRepository;
    }

    public UrlManagerState getState() {
        List<ManagedUrl> urls = managedUrlRepository.findAllByOrderByLabelAsc().stream()
            .map(UrlManagerModelMapper::toModel)
            .toList();

        List<UrlCardConfig> cards = urlCardConfigRepository.findAllByOrderByTitleAsc().stream()
            .map(UrlManagerModelMapper::toModel)
            .toList();

        return new UrlManagerState(urls, cards);
    }

    @Transactional
    public UrlManagerState replaceState(UrlManagerState state) {
        List<ManagedUrl> urls = state != null && state.getUrls() != null ? state.getUrls() : List.of();
        List<UrlCardConfig> cards = state != null && state.getCards() != null ? state.getCards() : List.of();

        validateUrls(urls);
        validateCards(cards);

        managedUrlRepository.deleteAllInBatch();
        urlCardConfigRepository.deleteAllInBatch();

        List<ManagedUrlEntity> urlEntities = urls.stream()
            .map(UrlManagerModelMapper::toEntity)
            .toList();

        List<UrlCardConfigEntity> cardEntities = cards.stream()
            .map(UrlManagerModelMapper::toEntity)
            .toList();

        managedUrlRepository.saveAll(urlEntities);
        urlCardConfigRepository.saveAll(cardEntities);

        return getState();
    }

    private void validateUrls(List<ManagedUrl> urls) {
        for (ManagedUrl url : urls) {
            requireText(url.getLabel(), "Le libelle d'une URL est obligatoire.");
            requireText(url.getUrl(), "L'URL cible est obligatoire.");
            url.setTags(normalizeTags(url.getTags()));
            url.setDescription(normalizeNullableText(url.getDescription()));
        }
    }

    private void validateCards(List<UrlCardConfig> cards) {
        for (UrlCardConfig card : cards) {
            requireText(card.getTitle(), "Le titre d'une carte est obligatoire.");
            card.setTags(normalizeTags(card.getTags()));

            if (card.getTags().isEmpty()) {
                throw new IllegalArgumentException("Une carte doit contenir au moins un tag.");
            }

            String matchMode = requireText(card.getMatchMode(), "Le mode de filtre est obligatoire.").toLowerCase();
            if (!"any".equals(matchMode) && !"all".equals(matchMode)) {
                throw new IllegalArgumentException("Le mode de filtre doit etre 'any' ou 'all'.");
            }

            card.setMatchMode(matchMode);
        }
    }

    private static List<String> normalizeTags(List<String> tags) {
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

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    private static String normalizeNullableText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
