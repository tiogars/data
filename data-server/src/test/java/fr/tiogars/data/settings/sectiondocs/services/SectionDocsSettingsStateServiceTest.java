package fr.tiogars.data.settings.sectiondocs.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.settings.sectiondocs.entities.SectionDocsSettingEntity;
import fr.tiogars.data.settings.sectiondocs.models.SectionDocsSetting;
import fr.tiogars.data.settings.sectiondocs.models.SectionDocsSettingsState;
import fr.tiogars.data.settings.sectiondocs.repositories.SectionDocsSettingRepository;
import fr.tiogars.data.docs.section.services.SectionDocsFilesystemSyncService;

@ExtendWith(MockitoExtension.class)
class SectionDocsSettingsStateServiceTest {

    @Mock
    private SectionDocsSettingRepository sectionDocsSettingRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService;

    @InjectMocks
    private SectionDocsSettingsStateService sectionDocsSettingsStateService;

    @Test
    void shouldNormalizeStoragePathForRootSection() {
        SectionEntity rootSection = new SectionEntity();
        rootSection.setId("root-1");
        rootSection.setName("Guides");

        when(sectionRepository.findById("root-1")).thenReturn(Optional.of(rootSection));
        when(sectionDocsSettingRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(sectionDocsSettingRepository.findAll()).thenReturn(List.of(createSavedEntity("setting-1", "root-1", "guides/produits")));

        SectionDocsSetting item = new SectionDocsSetting();
        item.setSectionId("root-1");
        item.setStoragePath("guides\\produits");

        SectionDocsSettingsState result = sectionDocsSettingsStateService.replaceState(new SectionDocsSettingsState(List.of(item)));

        assertThat(result.getItems())
            .singleElement()
            .satisfies(savedItem -> {
                assertThat(savedItem.getSectionId()).isEqualTo("root-1");
                assertThat(savedItem.getStoragePath()).isEqualTo("guides/produits");
            });
    }

    @Test
    void shouldThrowWhenSectionIsNotRoot() {
        SectionEntity parent = new SectionEntity();
        parent.setId("parent-1");

        SectionEntity child = new SectionEntity();
        child.setId("child-1");
        child.setParent(parent);

        when(sectionRepository.findById("child-1")).thenReturn(Optional.of(child));

        SectionDocsSetting item = new SectionDocsSetting();
        item.setSectionId("child-1");
        item.setStoragePath("guides/produits");

        SectionDocsSettingsState state = new SectionDocsSettingsState(List.of(item));

        assertThatThrownBy(() -> sectionDocsSettingsStateService.replaceState(state))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Seules les sections racines peuvent être configurées.");
    }

    private SectionDocsSettingEntity createSavedEntity(String id, String sectionId, String storagePath) {
        SectionDocsSettingEntity entity = new SectionDocsSettingEntity();
        entity.setId(id);
        entity.setSectionId(sectionId);
        entity.setStoragePath(storagePath);
        return entity;
    }
}