package fr.tiogars.data.cave.vinnom.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.cave.maison.repositories.MaisonRepository;
import fr.tiogars.data.cave.vinnom.models.VinNomListResponse;
import fr.tiogars.data.cave.vinnom.repositories.VinNomRepository;

@Service
public class VinNomExportService {
    private final VinNomRepository vinNomRepository;
    private final MaisonRepository maisonRepository;
    public VinNomExportService(VinNomRepository vinNomRepository, MaisonRepository maisonRepository) { this.vinNomRepository = vinNomRepository; this.maisonRepository = maisonRepository; }
    public VinNomListResponse exportVinNoms() {
        var entities = vinNomRepository.findAllByOrderByNameAsc();
        var items = VinNomModelMapper.toModels(entities, maisonRepository);
        return new VinNomListResponse(items, items.size());
    }
}
