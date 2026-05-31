package fr.tiogars.data.system.serverinfo.repositories;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Repository;

import fr.tiogars.data.system.serverinfo.models.JpaColumnInfo;
import fr.tiogars.data.system.serverinfo.models.JpaEntityAttributeInfo;
import fr.tiogars.data.system.serverinfo.models.JpaEntityClassInfo;
import fr.tiogars.data.system.serverinfo.models.JpaJoinColumnInfo;
import fr.tiogars.data.system.serverinfo.models.JpaManyToOneInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

@Repository
public class ServerJpaEntityInfoRepository {

    private static final String ROOT_SCAN_PACKAGE = "fr.tiogars.data";

    public List<JpaEntityClassInfo> listJpaEntityInfos() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        return scanner.findCandidateComponents(ROOT_SCAN_PACKAGE)
            .stream()
            .map(candidate -> buildEntityInfo(loadClass(candidate.getBeanClassName())))
            .sorted(Comparator.comparing(JpaEntityClassInfo::getSimpleClassName))
            .collect(Collectors.toList());
    }

    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Impossible de charger la classe entite: " + className, exception);
        }
    }

    private JpaEntityClassInfo buildEntityInfo(Class<?> entityClass) {
        JpaEntityClassInfo info = new JpaEntityClassInfo();
        Entity entity = entityClass.getAnnotation(Entity.class);
        Table table = entityClass.getAnnotation(Table.class);

        info.setClassName(entityClass.getName());
        info.setSimpleClassName(entityClass.getSimpleName());
        info.setEntityName(emptyToNull(entity.name()));
        info.setTableName(table != null ? emptyToNull(table.name()) : null);
        info.setTableSchema(table != null ? emptyToNull(table.schema()) : null);
        info.setTableCatalog(table != null ? emptyToNull(table.catalog()) : null);
        info.setAttributes(extractAttributes(entityClass));

        return info;
    }

    private List<JpaEntityAttributeInfo> extractAttributes(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
            .filter(field -> !field.isSynthetic())
            .filter(field -> !Modifier.isStatic(field.getModifiers()))
            .map(this::buildAttributeInfo)
            .sorted(Comparator.comparing(JpaEntityAttributeInfo::getName))
            .collect(Collectors.toList());
    }

    private JpaEntityAttributeInfo buildAttributeInfo(Field field) {
        JpaEntityAttributeInfo info = new JpaEntityAttributeInfo();
        Column column = field.getAnnotation(Column.class);
        GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);

        info.setName(field.getName());
        info.setType(field.getType().getSimpleName());
        info.setId(field.isAnnotationPresent(Id.class));
        info.setGenerated(generatedValue != null);
        info.setGenerationStrategy(generatedValue != null ? generatedValue.strategy().name() : null);
        info.setColumn(column != null ? toColumnInfo(column) : null);
        info.setManyToOne(manyToOne != null ? toManyToOneInfo(manyToOne) : null);
        info.setJoinColumn(joinColumn != null ? toJoinColumnInfo(joinColumn) : null);
        info.setVersion(field.isAnnotationPresent(Version.class));
        info.setLob(field.isAnnotationPresent(Lob.class));
        info.setTransientField(field.isAnnotationPresent(Transient.class));

        return info;
    }

    private JpaColumnInfo toColumnInfo(Column column) {
        JpaColumnInfo info = new JpaColumnInfo();
        info.setName(emptyToNull(column.name()));
        info.setNullable(column.nullable());
        info.setUpdatable(column.updatable());
        info.setInsertable(column.insertable());
        info.setUnique(column.unique());
        info.setLength(column.length());
        info.setPrecision(column.precision());
        info.setScale(column.scale());
        info.setColumnDefinition(emptyToNull(column.columnDefinition()));
        return info;
    }

    private JpaManyToOneInfo toManyToOneInfo(ManyToOne manyToOne) {
        JpaManyToOneInfo info = new JpaManyToOneInfo();
        info.setFetch(manyToOne.fetch().name());
        info.setOptional(manyToOne.optional());
        info.setCascade(Arrays.stream(manyToOne.cascade()).map(Enum::name).collect(Collectors.toList()));
        return info;
    }

    private JpaJoinColumnInfo toJoinColumnInfo(JoinColumn joinColumn) {
        JpaJoinColumnInfo info = new JpaJoinColumnInfo();
        info.setName(emptyToNull(joinColumn.name()));
        info.setReferencedColumnName(emptyToNull(joinColumn.referencedColumnName()));
        info.setNullable(joinColumn.nullable());
        info.setUpdatable(joinColumn.updatable());
        info.setInsertable(joinColumn.insertable());
        info.setUnique(joinColumn.unique());
        return info;
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }
}