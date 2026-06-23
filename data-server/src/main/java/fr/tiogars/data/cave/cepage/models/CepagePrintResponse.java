package fr.tiogars.data.cave.cepage.models;

import java.util.List;

public class CepagePrintResponse extends CepageListResponse {
    private String generatedAt;
    private int total;
    public CepagePrintResponse(List<Cepage> items, int count, String generatedAt, int total) {
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
