package fr.tiogars.data.dev.docs.serverinfo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.tiogars.data.dev.docs.serverinfo.models.JpaEntityAttributeInfo;
import fr.tiogars.data.dev.docs.serverinfo.models.JpaEntityClassInfo;

class ServerJpaEntityInfoRepositoryTest {

    @Test
    void shouldDiscoverSectionEntityAndReadJpaMetadata() {
        ServerJpaEntityInfoRepository repository = new ServerJpaEntityInfoRepository();

        JpaEntityClassInfo sectionEntity = repository.listJpaEntityInfos()
            .stream()
            .filter(entity -> "SectionEntity".equals(entity.getSimpleClassName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("SectionEntity should be discovered"));

        assertEquals("section", sectionEntity.getTableName());
        assertNotNull(sectionEntity.getAttributes());

        JpaEntityAttributeInfo idAttribute = sectionEntity.getAttributes()
            .stream()
            .filter(attribute -> "id".equals(attribute.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("id attribute should exist"));

        assertEquals("String", idAttribute.getType());
        assertTrue(idAttribute.isId());
        assertTrue(idAttribute.isGenerated());
        assertEquals("UUID", idAttribute.getGenerationStrategy());
        assertNotNull(idAttribute.getColumn());
        assertEquals("id", idAttribute.getColumn().getName());
        assertEquals(false, idAttribute.getColumn().isNullable());
        assertEquals(false, idAttribute.getColumn().isUpdatable());

        JpaEntityAttributeInfo parentAttribute = sectionEntity.getAttributes()
            .stream()
            .filter(attribute -> "parent".equals(attribute.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("parent attribute should exist"));

        assertNotNull(parentAttribute.getManyToOne());
        assertEquals("LAZY", parentAttribute.getManyToOne().getFetch());
        assertNotNull(parentAttribute.getJoinColumn());
        assertEquals("parent_id", parentAttribute.getJoinColumn().getName());
    }
}