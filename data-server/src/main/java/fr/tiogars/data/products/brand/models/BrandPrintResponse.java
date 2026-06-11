package fr.tiogars.data.products.brand.models;

import java.util.List;

public class BrandPrintResponse extends BrandListResponse {

    private String generatedAt;
    private int total;

    public BrandPrintResponse(List<Brand> items, int count, String generatedAt, int total) {
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
