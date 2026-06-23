package fr.tiogars.data.cave.vin.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.vin.entities.VinCepageEntity;
import fr.tiogars.data.cave.vin.entities.VinCirconstanceEntity;
import fr.tiogars.data.cave.vin.entities.VinEntity;
import fr.tiogars.data.cave.vin.entities.VinVinTagEntity;
import fr.tiogars.data.cave.vin.forms.VinCepageEntryForm;
import fr.tiogars.data.cave.vin.forms.VinCreationForm;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.repositories.VinCepageRepository;
import fr.tiogars.data.cave.vin.repositories.VinCirconstanceRepository;
import fr.tiogars.data.cave.vin.repositories.VinRepository;
import fr.tiogars.data.cave.vin.repositories.VinVinTagRepository;

@Service
public class VinCreationService {

    private final VinRepository vinRepository;
    private final VinCepageRepository vinCepageRepository;
    private final VinCirconstanceRepository vinCirconstanceRepository;
    private final VinVinTagRepository vinVinTagRepository;
    private final VinGetOneService vinGetOneService;

    public VinCreationService(
        VinRepository vinRepository,
        VinCepageRepository vinCepageRepository,
        VinCirconstanceRepository vinCirconstanceRepository,
        VinVinTagRepository vinVinTagRepository,
        VinGetOneService vinGetOneService
    ) {
        this.vinRepository = vinRepository;
        this.vinCepageRepository = vinCepageRepository;
        this.vinCirconstanceRepository = vinCirconstanceRepository;
        this.vinVinTagRepository = vinVinTagRepository;
        this.vinGetOneService = vinGetOneService;
    }

    @Transactional
    public Vin createVin(VinCreationForm form) {
        VinEntity entity = new VinEntity();
        applyValues(entity, form);
        VinEntity saved = vinRepository.save(entity);
        saveRelations(saved.getId(), form);
        return vinGetOneService.getVin(saved.getId());
    }

    static void applyValues(VinEntity entity, VinCreationForm form) {
        VinCreationForm safeForm = form != null ? form : new VinCreationForm();
        entity.setAppellationId(normalizeNullableText(safeForm.getAppellationId()));
        entity.setCouleurId(normalizeNullableText(safeForm.getCouleurId()));
        entity.setTypeVinId(normalizeNullableText(safeForm.getTypeVinId()));
        entity.setMaisonId(normalizeNullableText(safeForm.getMaisonId()));
        entity.setVinNomId(normalizeNullableText(safeForm.getVinNomId()));
        entity.setContenantId(normalizeNullableText(safeForm.getContenantId()));
        entity.setAnnee(safeForm.getAnnee());
        entity.setCommune(normalizeNullableText(safeForm.getCommune()));
        entity.setRegion(normalizeNullableText(safeForm.getRegion()));
        entity.setCommentaires(normalizeNullableText(safeForm.getCommentaires()));
        entity.setAccordsMetsVins(normalizeNullableText(safeForm.getAccordsMetsVins()));
    }

    void saveRelations(String vinId, VinCreationForm form) {
        for (VinCepageEntryForm cepageForm : sanitizeCepages(form != null ? form.getCepages() : null)) {
            VinCepageEntity cepageEntity = new VinCepageEntity();
            cepageEntity.setVinId(vinId);
            cepageEntity.setCepageId(requireText(cepageForm.getCepageId(), "Le cépage est obligatoire."));
            cepageEntity.setPourcentage(validatePourcentage(cepageForm.getPourcentage()));
            vinCepageRepository.save(cepageEntity);
        }

        for (String circonstanceId : sanitizeIds(form != null ? form.getCirconstanceIds() : null)) {
            VinCirconstanceEntity circonstanceEntity = new VinCirconstanceEntity();
            circonstanceEntity.setVinId(vinId);
            circonstanceEntity.setCirconstanceId(circonstanceId);
            vinCirconstanceRepository.save(circonstanceEntity);
        }

        for (String tagId : sanitizeIds(form != null ? form.getTagIds() : null)) {
            VinVinTagEntity tagEntity = new VinVinTagEntity();
            tagEntity.setVinId(vinId);
            tagEntity.setVinTagId(tagId);
            vinVinTagRepository.save(tagEntity);
        }
    }

    static Integer validatePourcentage(Integer pourcentage) {
        if (pourcentage == null) {
            return null;
        }
        if (pourcentage < 0 || pourcentage > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être compris entre 0 et 100.");
        }
        return pourcentage;
    }

    static List<VinCepageEntryForm> sanitizeCepages(List<VinCepageEntryForm> cepages) {
        if (cepages == null || cepages.isEmpty()) {
            return List.of();
        }
        return cepages.stream()
            .filter(item -> item != null && normalizeNullableText(item.getCepageId()) != null)
            .toList();
    }

    static List<String> sanitizeIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        Set<String> uniqueIds = new LinkedHashSet<>();
        for (String id : ids) {
            String normalized = normalizeNullableText(id);
            if (normalized != null) {
                uniqueIds.add(normalized);
            }
        }
        return uniqueIds.stream().toList();
    }
}
