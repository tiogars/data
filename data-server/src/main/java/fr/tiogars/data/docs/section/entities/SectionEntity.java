package fr.tiogars.data.docs.section.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;

@Entity
@Table(name = "section")
public class SectionEntity {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SectionEntity parent;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private SectionDocumentEntity document;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public SectionEntity getParent() {
        return parent;
    }

    public void setParent(SectionEntity parent) {
        this.parent = parent;
    }

    public SectionDocumentEntity getDocument() {
        return document;
    }

    public void setDocument(SectionDocumentEntity document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "SectionEntity [id=" + id + ", name=" + name + ", description=" + description + ", displayOrder="
            + displayOrder + ", parentId=" + (parent != null ? parent.getId() : null)
            + ", documentId=" + (document != null ? document.getId() : null) + "]";
    }
}