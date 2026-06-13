package fr.tiogars.data.sync.services;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class SyncCursorCodec {

    public CursorState decode(String cursorToken) {
        if (cursorToken == null || cursorToken.isBlank()) {
            return new CursorState(0, null, null);
        }

        try {
            String decoded = new String(Base64.getUrlDecoder().decode(cursorToken), StandardCharsets.UTF_8);
            String[] parts = decoded.split("\\|", -1);

            if (parts.length != 4 || !"v2".equals(parts[0])) {
                throw new IllegalArgumentException("Cursor invalide.");
            }

            int offset = Integer.parseInt(parts[1]);
            String updatedAfter = parts[2].isBlank() ? null : parts[2];
            Instant windowEnd = parts[3].isBlank() ? null : Instant.parse(parts[3]);
            return new CursorState(Math.max(offset, 0), updatedAfter, windowEnd);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Cursor invalide.", ex);
        }
    }

    public String encode(int nextOffset, String updatedAfter, Instant windowEnd) {
        String safeUpdatedAfter = updatedAfter == null ? "" : updatedAfter;
        String safeWindowEnd = windowEnd == null ? "" : windowEnd.toString();
        String payload = "v2|" + Math.max(nextOffset, 0) + "|" + safeUpdatedAfter + "|" + safeWindowEnd;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    public record CursorState(int offset, String updatedAfter, Instant windowEnd) {
    }
}
