package fr.tiogars.data.dev.docs.serverinfo.models;

public class JpaEntityAttributeInfo {

    private String name;
    private String type;
    private boolean id;
    private boolean generated;
    private String generationStrategy;
    private JpaColumnInfo column;
    private JpaManyToOneInfo manyToOne;
    private JpaJoinColumnInfo joinColumn;
    private boolean version;
    private boolean lob;
    private boolean transientField;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public String getGenerationStrategy() {
        return generationStrategy;
    }

    public void setGenerationStrategy(String generationStrategy) {
        this.generationStrategy = generationStrategy;
    }

    public JpaColumnInfo getColumn() {
        return column;
    }

    public void setColumn(JpaColumnInfo column) {
        this.column = column;
    }

    public JpaManyToOneInfo getManyToOne() {
        return manyToOne;
    }

    public void setManyToOne(JpaManyToOneInfo manyToOne) {
        this.manyToOne = manyToOne;
    }

    public JpaJoinColumnInfo getJoinColumn() {
        return joinColumn;
    }

    public void setJoinColumn(JpaJoinColumnInfo joinColumn) {
        this.joinColumn = joinColumn;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }

    public boolean isLob() {
        return lob;
    }

    public void setLob(boolean lob) {
        this.lob = lob;
    }

    public boolean isTransientField() {
        return transientField;
    }

    public void setTransientField(boolean transientField) {
        this.transientField = transientField;
    }
}