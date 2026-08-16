package fr.tiogars.data.cave.vin.services;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;
import fr.tiogars.data.cave.vin.entities.VinCepageEntity;
import fr.tiogars.data.cave.vin.entities.VinCirconstanceEntity;
import fr.tiogars.data.cave.vin.entities.VinEntity;
import fr.tiogars.data.cave.vin.entities.VinVinTagEntity;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;
import fr.tiogars.data.cave.vintag.repositories.VinTagRepository;

@Service
public class VinLookupHelper {

    private final AppellationRepository appellationRepository;
    private final CouleurRepository couleurRepository;
    private final TypeVinRepository typeVinRepository;
    private final MaisonRepository maisonRepository;
    private final VinNomRepository vinNomRepository;
    private final ContenantRepository contenantRepository;
    private final CepageRepository cepageRepository;
    private final CirconstanceRepository circonstanceRepository;
    private final VinTagRepository vinTagRepository;

    public VinLookupHelper(
        AppellationRepository appellationRepository,
        CouleurRepository couleurRepository,
        TypeVinRepository typeVinRepository,
        MaisonRepository maisonRepository,
        VinNomRepository vinNomRepository,
        ContenantRepository contenantRepository,
        CepageRepository cepageRepository,
        CirconstanceRepository circonstanceRepository,
        VinTagRepository vinTagRepository
    ) {
        this.appellationRepository = appellationRepository;
        this.couleurRepository = couleurRepository;
        this.typeVinRepository = typeVinRepository;
        this.maisonRepository = maisonRepository;
        this.vinNomRepository = vinNomRepository;
        this.contenantRepository = contenantRepository;
        this.cepageRepository = cepageRepository;
        this.circonstanceRepository = circonstanceRepository;
        this.vinTagRepository = vinTagRepository;
    }

    public VinResolutionContext buildResolutionContext(
        Collection<VinEntity> vins,
        Collection<VinCepageEntity> cepages,
        Collection<VinCirconstanceEntity> circonstances,
        Collection<VinVinTagEntity> tags
    ) {
        Set<String> appellationIds = collectIds(vins, entity -> Objects.requireNonNull(entity).getAppellationId());
        Set<String> couleurIds = collectIds(vins, entity -> Objects.requireNonNull(entity).getCouleurId());
        Set<String> typeVinIds = collectIds(vins, entity -> Objects.requireNonNull(entity).getTypeVinId());
        Set<String> maisonIds = collectIds(vins, entity -> Objects.requireNonNull(entity).getMaisonId());
        Set<String> vinNomIds = collectIds(vins, entity -> Objects.requireNonNull(entity).getVinNomId());
        Set<String> contenantIds = collectIds(vins, entity -> Objects.requireNonNull(entity).getContenantId());
        Set<String> cepageIds = collectIds(cepages, entity -> Objects.requireNonNull(entity).getCepageId());
        Set<String> circonstanceIds = collectIds(circonstances, entity -> Objects.requireNonNull(entity).getCirconstanceId());
        Set<String> tagIds = collectIds(tags, entity -> Objects.requireNonNull(entity).getVinTagId());

        return new VinResolutionContext(
            loadNames(appellationIds, appellationRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName()),
            loadNames(couleurIds, couleurRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName()),
            loadNames(typeVinIds, typeVinRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName()),
            loadNames(maisonIds, maisonRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName()),
            loadNames(vinNomIds, vinNomRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName()),
            loadNames(contenantIds, contenantRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName()),
            loadNames(cepageIds, cepageRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName()),
            loadNames(circonstanceIds, circonstanceRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName()),
            loadNames(tagIds, vinTagRepository, entity -> Objects.requireNonNull(entity).getId(), entity -> Objects.requireNonNull(entity).getName())
        );
    }

    private static <T> Set<String> collectIds(Collection<T> items, NonNullFunction<T, String> extractor) {
        if (items == null || items.isEmpty()) {
            return Set.of();
        }
        return items.stream()
            .map(extractor)
            .filter(value -> value != null && !value.isBlank())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static <T> Map<String, String> loadNames(
        Set<String> ids,
        JpaRepository<T, String> repository,
        NonNullFunction<T, String> idExtractor,
        NonNullFunction<T, String> nameExtractor
    ) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return StreamSupport.stream(repository.findAllById(ids).spliterator(), false)
            .filter(entity -> idExtractor.apply(entity) != null)
            .collect(Collectors.toMap(idExtractor, nameExtractor, (left, right) -> left));
    }

    @FunctionalInterface
    private interface NonNullFunction<T, R> extends Function<T, R> {
        @Override
        R apply(@NonNull T value);
    }
}
