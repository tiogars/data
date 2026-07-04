package fr.tiogars.data.docs.sectiondocument.models;

import java.util.List;

public class SectionDocumentListResponse {

    private List<SectionDocument> items;
    private int count;

    public SectionDocumentListResponse() {
    }

    public SectionDocumentListResponse(List<SectionDocument> items, int count) {
        this.items = items;
        this.count = count;
    }

    public List<SectionDocument> getItems() {
        return items;
    }

    public void setItems(List<SectionDocument> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
