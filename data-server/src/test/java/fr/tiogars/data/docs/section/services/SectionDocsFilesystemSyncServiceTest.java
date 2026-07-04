package fr.tiogars.data.docs.section.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.settings.sectiondocs.entities.SectionDocsSettingEntity;
import fr.tiogars.data.settings.sectiondocs.repositories.SectionDocsSettingRepository;

@ExtendWith(MockitoExtension.class)
class SectionDocsFilesystemSyncServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private SectionDocsSettingRepository sectionDocsSettingRepository;

    @TempDir
    Path tempDir;

    @Test
    void shouldGenerateNestedMarkdownTreeUsingHierarchicalIndex() throws Exception {
        SectionEntity root = new SectionEntity();
        root.setId("root-1");
        root.setName("Guides");
        root.setDescription("Documentation principale");
        root.setDisplayOrder(1);

        SectionEntity child = new SectionEntity();
        child.setId("child-1");
        child.setName("Installation");
        child.setDescription("Procedure d'installation");
        child.setDisplayOrder(2);
        child.setParent(root);

        SectionDocsSettingEntity setting = new SectionDocsSettingEntity();
        setting.setId("setting-1");
        setting.setSectionId("root-1");
        setting.setStoragePath("guides");

        when(sectionRepository.findAll(any(Sort.class))).thenReturn(List.of(root, child));
        when(sectionDocsSettingRepository.findBySectionId("root-1")).thenReturn(Optional.of(setting));

        SectionDocsFilesystemSyncService service = new SectionDocsFilesystemSyncService(
            sectionRepository,
            sectionDocsSettingRepository,
            tempDir.resolve("docs").toString()
        );

        service.syncAfterSectionCreated("child-1");

        Path rootIndex = tempDir.resolve("docs").resolve("guides").resolve("1-Guides").resolve("index.md");
        Path childIndex = tempDir.resolve("docs").resolve("guides").resolve("1-Guides").resolve("1.2-Installation").resolve("index.md");

        assertThat(rootIndex).exists();
        assertThat(childIndex).exists();
        assertThat(normalizeLineEndings(Files.readString(rootIndex))).isEqualTo("# 1-Guides\n\nDocumentation principale");
        assertThat(normalizeLineEndings(Files.readString(childIndex))).isEqualTo("# 1.2-Installation\n\nProcedure d'installation");
    }

    private String normalizeLineEndings(String value) {
        return value.replace("\r\n", "\n").replace("\r", "\n");
    }
}