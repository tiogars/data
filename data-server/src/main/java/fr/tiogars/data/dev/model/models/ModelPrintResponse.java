package fr.tiogars.data.dev.model.models;

import java.util.List;

public class ModelPrintResponse extends ModelListResponse {

    private String generatedAt;
    private int total;

    public ModelPrintResponse(List<Model> items, int count, String generatedAt, int total) {
        super(items, count);
        this.generatedAt = generatedAt;
        this.total = total;
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
