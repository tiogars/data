package fr.tiogars.data.cave.vin.services;

import java.util.ArrayList;
import java.util.List;

import fr.tiogars.data.cave.vin.entities.VinCepageEntity;
import fr.tiogars.data.cave.vin.entities.VinCirconstanceEntity;
import fr.tiogars.data.cave.vin.entities.VinEntity;
import fr.tiogars.data.cave.vin.entities.VinVinTagEntity;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.models.VinCepageEntry;

final class VinModelMapper {

    private VinModelMapper() {
    }

    static Vin toModel(
        VinEntity entity,
        List<VinCepageEntity> cepageEntities,
        List<VinCirconstanceEntity> circonstanceEntities,
        List<VinVinTagEntity> tagEntities,
        VinResolutionContext context
    ) {
        Vin model = new Vin();
        model.setId(entity.getId());
        model.setAppellationId(entity.getAppellationId());
        model.setAppellationName(context.appellationNames().get(entity.getAppellationId()));
        model.setCouleurId(entity.getCouleurId());
        model.setCouleurName(context.couleurNames().get(entity.getCouleurId()));
        model.setTypeVinId(entity.getTypeVinId());
        model.setTypeVinName(context.typeVinNames().get(entity.getTypeVinId()));
        model.setMaisonId(entity.getMaisonId());
        model.setMaisonName(context.maisonNames().get(entity.getMaisonId()));
        model.setVinNomId(entity.getVinNomId());
        model.setVinNomName(context.vinNomNames().get(entity.getVinNomId()));
        model.setContenantId(entity.getContenantId());
        model.setContenantName(context.contenantNames().get(entity.getContenantId()));
        model.setAnnee(entity.getAnnee());
        model.setCommune(entity.getCommune());
        model.setRegion(entity.getRegion());
        model.setCommentaires(entity.getCommentaires());
        model.setAccordsMetsVins(entity.getAccordsMetsVins());
        model.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        model.setUpdatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null);

        List<VinCepageEntry> cepages = new ArrayList<>();
        for (VinCepageEntity cepageEntity : safeList(cepageEntities)) {
            VinCepageEntry cepage = new VinCepageEntry();
            cepage.setCepageId(cepageEntity.getCepageId());
            cepage.setCepageName(context.cepageNames().get(cepageEntity.getCepageId()));
            cepage.setPourcentage(cepageEntity.getPourcentage());
            cepages.add(cepage);
        }
        model.setCepages(cepages);

        List<String> circonstances = new ArrayList<>();
        List<String> circonstanceNames = new ArrayList<>();
        for (VinCirconstanceEntity circonstanceEntity : safeList(circonstanceEntities)) {
            circonstances.add(circonstanceEntity.getCirconstanceId());
            String name = context.circonstanceNames().get(circonstanceEntity.getCirconstanceId());
            if (name != null) {
                circonstanceNames.add(name);
            }
        }
        model.setCirconstances(circonstances);
        model.setCirconstanceNames(circonstanceNames);

        List<String> tags = new ArrayList<>();
        List<String> tagNames = new ArrayList<>();
        for (VinVinTagEntity tagEntity : safeList(tagEntities)) {
            tags.add(tagEntity.getVinTagId());
            String name = context.tagNames().get(tagEntity.getVinTagId());
            if (name != null) {
                tagNames.add(name);
            }
        }
        model.setTags(tags);
        model.setTagNames(tagNames);
        return model;
    }

    private static <T> List<T> safeList(List<T> values) {
        return values != null ? values : List.of();
    }
}
