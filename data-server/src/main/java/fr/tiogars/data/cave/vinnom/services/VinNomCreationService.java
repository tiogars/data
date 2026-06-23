package fr.tiogars.data.cave.vinnom.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.entities.VinNomEntity;
import fr.tiogars.data.cave.vinnom.forms.VinNomCreationForm;
import fr.tiogars.data.cave.vinnom.models.VinNom;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomCreationService {
    private final VinNomRepository vinNomRepository;
    private final MaisonRepository maisonRepository;
    public VinNomCreationService(VinNomRepository vinNomRepository, MaisonRepository maisonRepository) { this.vinNomRepository = vinNomRepository; this.maisonRepository = maisonRepository; }
    public VinNom createVinNom(VinNomCreationForm form) {
        validateUniqueCombination(form.getName(), form.getMaisonId(), null);
        VinNomEntity entity = new VinNomEntity();
        applyValues(entity, form.getName(), form.getMaisonId(), maisonRepository);
        return VinNomModelMapper.toModel(vinNomRepository.save(entity), maisonRepository);
    }
    static void applyValues(VinNomEntity entity, String name, String maisonId, MaisonRepository maisonRepository) {
        entity.setName(requireText(name, "Le nom du vin est obligatoire."));
        entity.setMaisonId(resolveMaisonId(maisonId, maisonRepository));
    }
    void validateUniqueCombination(String name, String maisonId, String currentId) {
        String normalizedName = requireText(name, "Le nom du vin est obligatoire.");
        String normalizedMaisonId = normalizeNullableText(maisonId);
        Optional<VinNomEntity> existing = normalizedMaisonId == null ? vinNomRepository.findByNameAndMaisonIdIsNull(normalizedName) : vinNomRepository.findByNameAndMaisonId(normalizedName, normalizedMaisonId);
        existing.filter(entity -> !entity.getId().equals(currentId)).ifPresent(entity -> { throw new IllegalArgumentException("Un vin avec ce nom existe deja pour cette maison."); });
    }
    private static String resolveMaisonId(String maisonId, MaisonRepository maisonRepository) {
        String normalizedMaisonId = normalizeNullableText(maisonId);
        if (normalizedMaisonId == null) return null;
        if (!maisonRepository.existsById(normalizedMaisonId)) throw new DataNotFoundException("Maison non trouvee pour l'id: " + normalizedMaisonId);
        return normalizedMaisonId;
    }
}
