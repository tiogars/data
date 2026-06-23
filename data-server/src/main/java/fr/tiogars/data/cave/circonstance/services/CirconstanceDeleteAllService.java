package fr.tiogars.data.cave.circonstance.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.circonstance.repositories.CirconstanceRepository;

@Service
public class CirconstanceDeleteAllService {
    private final CirconstanceRepository circonstanceRepository;
    public CirconstanceDeleteAllService(CirconstanceRepository circonstanceRepository) { this.circonstanceRepository = circonstanceRepository; }
    public void deleteAllCirconstances() { circonstanceRepository.deleteAllInBatch(); }
}
