package fr.tiogars.data.cave.appellation.models;

import java.util.List;

public class AppellationPrintResponse extends AppellationListResponse {
    private String generatedAt;
    private int total;
    public AppellationPrintResponse(List<Appellation> items, int count, String generatedAt, int total) {
        super(items, count); this.generatedAt = generatedAt; this.total = total;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
