package fr.tiogars.data.dev.docs.serverinfo.models;

import java.util.List;

public class JpaManyToOneInfo {

    private String fetch;
    private boolean optional;
    private List<String> cascade;

    public String getFetch() {
        return fetch;
    }

    public void setFetch(String fetch) {
        this.fetch = fetch;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public List<String> getCascade() {
        return cascade;
    }

    public void setCascade(List<String> cascade) {
        this.cascade = cascade;
    }
}