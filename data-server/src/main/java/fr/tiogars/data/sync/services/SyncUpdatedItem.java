package fr.tiogars.data.sync.services;

import java.time.Instant;

public interface SyncUpdatedItem {

    String getId();

    Instant getUpdatedAt();
}
