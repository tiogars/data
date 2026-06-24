package fr.tiogars.data.cave.vin.services;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.appellation.entities.AppellationEntity;
import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;
import fr.tiogars.data.cave.cepage.entities.CepageEntity;
import fr.tiogars.data.cave.cepage.repositories.CepageRepository;
import fr.tiogars.data.cave.circonstance.entities.CirconstanceEntity;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;
import fr.tiogars.data.cave.contenant.entities.ContenantEntity;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;
import fr.tiogars.data.cave.couleur.entities.CouleurEntity;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;
import fr.tiogars.data.cave.maison.entities.MaisonEntity;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.typevin.entities.TypeVinEntity;
import fr.tiogars.data.cave.typevin.repositories.TypeVinRepository;
import fr.tiogars.data.cave.vin.entities.VinCepageEntity;
import fr.tiogars.data.cave.vin.entities.VinCirconstanceEntity;
import fr.tiogars.data.cave.vin.entities.VinEntity;
import fr.tiogars.data.cave.vin.entities.VinVinTagEntity;
import fr.tiogars.data.cave.vinnom.entities.VinNomEntity;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;
import fr.tiogars.data.cave.vintag.entities.VinTagEntity;
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
        Set<String> appellationIds = collectIds(vins, VinEntity::getAppellationId);
        Set<String> couleurIds = collectIds(vins, VinEntity::getCouleurId);
        Set<String> typeVinIds = collectIds(vins, VinEntity::getTypeVinId);
        Set<String> maisonIds = collectIds(vins, VinEntity::getMaisonId);
        Set<String> vinNomIds = collectIds(vins, VinEntity::getVinNomId);
        Set<String> contenantIds = collectIds(vins, VinEntity::getContenantId);
        Set<String> cepageIds = collectIds(cepages, VinCepageEntity::getCepageId);
        Set<String> circonstanceIds = collectIds(circonstances, VinCirconstanceEntity::getCirconstanceId);
        Set<String> tagIds = collectIds(tags, VinVinTagEntity::getVinTagId);

        return new VinResolutionContext(
            loadNames(appellationIds, appellationRepository, AppellationEntity::getId, AppellationEntity::getName),
            loadNames(couleurIds, couleurRepository, CouleurEntity::getId, CouleurEntity::getName),
            loadNames(typeVinIds, typeVinRepository, TypeVinEntity::getId, TypeVinEntity::getName),
            loadNames(maisonIds, maisonRepository, MaisonEntity::getId, MaisonEntity::getName),
            loadNames(vinNomIds, vinNomRepository, VinNomEntity::getId, VinNomEntity::getName),
            loadNames(contenantIds, contenantRepository, ContenantEntity::getId, ContenantEntity::getName),
            loadNames(cepageIds, cepageRepository, CepageEntity::getId, CepageEntity::getName),
            loadNames(circonstanceIds, circonstanceRepository, CirconstanceEntity::getId, CirconstanceEntity::getName),
            loadNames(tagIds, vinTagRepository, VinTagEntity::getId, VinTagEntity::getName)
        );
    }

    private static <T> Set<String> collectIds(Collection<T> items, Function<T, String> extractor) {
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
        Function<T, String> idExtractor,
        Function<T, String> nameExtractor
    ) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return StreamSupport.stream(repository.findAllById(ids).spliterator(), false)
            .filter(entity -> idExtractor.apply(entity) != null)
            .collect(Collectors.toMap(idExtractor, nameExtractor, (left, right) -> left));
    }
}
