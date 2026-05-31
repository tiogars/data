package fr.tiogars.data.system.serverinfo.models;

import java.util.List;

public class JpaEntityClassInfo {

    private String className;
    private String simpleClassName;
    private String entityName;
    private String tableName;
    private String tableSchema;
    private String tableCatalog;
    private List<JpaEntityAttributeInfo> attributes;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    public List<JpaEntityAttributeInfo> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<JpaEntityAttributeInfo> attributes) {
        this.attributes = attributes;
    }
}