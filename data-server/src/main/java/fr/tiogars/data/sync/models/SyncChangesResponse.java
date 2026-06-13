package fr.tiogars.data.sync.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Reponse de synchronisation incrementale par domaine.")
public class SyncChangesResponse<T> {

    @Schema(description = "Elements crees/modifies dans la fenetre de synchronisation.")
    private List<T> items;

    @Schema(description = "Identifiants distants supprimes dans la fenetre de synchronisation.", example = "[\"id-1\", \"id-2\"]")
    private List<String> deletedIds;

    @Schema(description = "Token opaque de reprise pour l'appel suivant.", example = "djF8MTAwfDIwMjYtMDYtMTNUMTI6MDA6MDBa")
    private String nextCursor;

    @Schema(description = "Indique si d'autres elements restent a recuperer.", example = "true")
    private boolean hasMore;

    @Schema(description = "Nombre d'elements retournes dans cette reponse.", example = "100")
    private int count;

    public SyncChangesResponse() {
    }

    public SyncChangesResponse(List<T> items, List<String> deletedIds, String nextCursor, boolean hasMore, int count) {
        this.items = items;
        this.deletedIds = deletedIds;
        this.nextCursor = nextCursor;
        this.hasMore = hasMore;
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public List<String> getDeletedIds() {
        return deletedIds;
    }

    public void setDeletedIds(List<String> deletedIds) {
        this.deletedIds = deletedIds;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
