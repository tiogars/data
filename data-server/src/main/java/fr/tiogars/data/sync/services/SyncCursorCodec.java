package fr.tiogars.data.sync.services;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import org.springframework.stereotype.Component;

/**
 * Encode et decode le curseur de synchronisation base sur une pagination keyset
 * (derniere ligne lue : {@code updatedAt} puis {@code id}).
 */
@Component
public class SyncCursorCodec {

    private static final String VERSION = "v3";
    private static final String LEGACY_VERSION = "v2";

    public CursorState decode(String cursorToken) {
        if (cursorToken == null || cursorToken.isBlank()) {
            return new CursorState(null, null, null, null);
        }

        try {
            String decoded = new String(Base64.getUrlDecoder().decode(cursorToken), StandardCharsets.UTF_8);
            String[] parts = decoded.split("\\|", -1);

            if (parts.length == 5 && VERSION.equals(parts[0])) {
                Instant lastUpdatedAt = parts[1].isBlank() ? null : Instant.parse(parts[1]);
                String lastId = parts[2].isBlank() ? null : parts[2];
                String updatedAfter = parts[3].isBlank() ? null : parts[3];
                Instant windowEnd = parts[4].isBlank() ? null : Instant.parse(parts[4]);
                return new CursorState(lastUpdatedAt, lastId, updatedAfter, windowEnd);
            }

            // Curseur historique base sur un offset : la fenetre est conservee, la page repart du debut.
            if (parts.length == 4 && LEGACY_VERSION.equals(parts[0])) {
                String updatedAfter = parts[2].isBlank() ? null : parts[2];
                Instant windowEnd = parts[3].isBlank() ? null : Instant.parse(parts[3]);
                return new CursorState(null, null, updatedAfter, windowEnd);
            }

            throw new IllegalArgumentException("Cursor invalide.");
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Cursor invalide.", ex);
        }
    }

    public String encode(Instant lastUpdatedAt, String lastId, String updatedAfter, Instant windowEnd) {
        String payload = VERSION
            + "|" + (lastUpdatedAt == null ? "" : lastUpdatedAt.toString())
            + "|" + (lastId == null ? "" : lastId)
            + "|" + (updatedAfter == null ? "" : updatedAfter)
            + "|" + (windowEnd == null ? "" : windowEnd.toString());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    public record CursorState(Instant lastUpdatedAt, String lastId, String updatedAfter, Instant windowEnd) {
    }
}
