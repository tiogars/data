package fr.tiogars.data.cave.vin.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.cave.vin.forms.VinCepageEntryForm;
import fr.tiogars.data.cave.vin.forms.VinCreationForm;
import fr.tiogars.data.cave.vin.forms.VinImportForm;
import fr.tiogars.data.cave.vin.models.Vin;
import fr.tiogars.data.cave.vin.models.VinCepageEntry;
import fr.tiogars.data.cave.vin.models.VinImportResult;
import fr.tiogars.data.cave.vin.repositories.VinRepository;
import fr.tiogars.data.common.validation.TextValidationUtils;

@Service
public class VinImportService {

    private final VinRepository vinRepository;
    private final VinCreationService vinCreationService;

    public VinImportService(VinRepository vinRepository, VinCreationService vinCreationService) {
        this.vinRepository = vinRepository;
        this.vinCreationService = vinCreationService;
    }

    @Transactional
    public VinImportResult importVins(VinImportForm form) {
        List<Vin> items = form != null && form.getItems() != null ? form.getItems() : List.of();
        if (items.isEmpty()) {
            return new VinImportResult(List.of(), 0, 0, 0, 0);
        }

        List<Vin> imported = new ArrayList<>();
        int alreadyExistsCount = 0;
        int invalidCount = 0;

        for (Vin item : items) {
            if (item == null) {
                invalidCount++;
                continue;
            }
            String existingId = TextValidationUtils.normalizeNullableText(item.getId());
            if (existingId != null && vinRepository.existsById(existingId)) {
                alreadyExistsCount++;
                continue;
            }
            try {
                imported.add(vinCreationService.createVin(toCreationForm(item)));
            } catch (RuntimeException ex) {
                invalidCount++;
            }
        }

        int addedCount = imported.size();
        return new VinImportResult(imported, addedCount, alreadyExistsCount + invalidCount, alreadyExistsCount, invalidCount);
    }

    private VinCreationForm toCreationForm(Vin item) {
        VinCreationForm form = new VinCreationForm();
        form.setAppellationId(item.getAppellationId());
        form.setCouleurId(item.getCouleurId());
        form.setTypeVinId(item.getTypeVinId());
        form.setMaisonId(item.getMaisonId());
        form.setVinNomId(item.getVinNomId());
        form.setContenantId(item.getContenantId());
        form.setAnnee(item.getAnnee());
        form.setCommune(item.getCommune());
        form.setRegion(item.getRegion());
        form.setCommentaires(item.getCommentaires());
        form.setAccordsMetsVins(item.getAccordsMetsVins());
        form.setCirconstanceIds(item.getCirconstances());
        form.setTagIds(item.getTags());
        form.setCepages(toCepageForms(item.getCepages()));
        return form;
    }

    private List<VinCepageEntryForm> toCepageForms(List<VinCepageEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return List.of();
        }
        List<VinCepageEntryForm> forms = new ArrayList<>();
        for (VinCepageEntry entry : entries) {
            if (entry == null) {
                continue;
            }
            VinCepageEntryForm form = new VinCepageEntryForm();
            form.setCepageId(entry.getCepageId());
            form.setPourcentage(entry.getPourcentage());
            forms.add(form);
        }
        return forms;
    }
}
