package fr.tiogars.data.sync.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;
import fr.tiogars.data.sync.models.SyncChangesResponse;

@SpringBootTest
class SyncChangesServiceIntegrationTest {

    @Autowired
    private SyncChangesService syncChangesService;

    @Autowired
    private GtinRepository gtinRepository;

    @BeforeEach
    void cleanData() {
        gtinRepository.deleteAllInBatch();
    }

    @Test
    void shouldPaginateGtinChangesWithKeysetCursor() {
        List<String> savedIds = List.of(persistGtin(), persistGtin(), persistGtin())
            .stream()
            .sorted()
            .toList();

        SyncChangesResponse<Gtin> firstPage = syncChangesService.getGtinChanges(null, null, 2);

        assertThat(firstPage.getItems()).hasSize(2);
        assertThat(firstPage.isHasMore()).isTrue();
        assertThat(firstPage.getNextCursor()).isNotBlank();

        SyncChangesResponse<Gtin> secondPage = syncChangesService.getGtinChanges(firstPage.getNextCursor(), null, 2);

        assertThat(secondPage.getItems()).hasSize(1);
        assertThat(secondPage.isHasMore()).isFalse();
        assertThat(secondPage.getNextCursor()).isNull();

        List<String> returnedIds = java.util.stream.Stream
            .concat(firstPage.getItems().stream(), secondPage.getItems().stream())
            .map(Gtin::getId)
            .sorted()
            .toList();

        assertThat(returnedIds).isEqualTo(savedIds);
    }

    @Test
    void shouldReturnEmptyPageWhenUpdatedAfterIsInTheFuture() {
        persistGtin();

        SyncChangesResponse<Gtin> response = syncChangesService.getGtinChanges(
            null,
            java.time.Instant.now().plusSeconds(3600).toString(),
            10
        );

        assertThat(response.getItems()).isEmpty();
        assertThat(response.isHasMore()).isFalse();
    }

    private String persistGtin() {
        GtinEntity entity = new GtinEntity();
        entity.setCode(UUID.randomUUID().toString().substring(0, 12));
        entity.setDescription("Produit de test");
        return gtinRepository.saveAndFlush(entity).getId();
    }
}
