package fr.tiogars.data.docs.section.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.forms.SectionCreationForm;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.repositories.SectionRepository;

@ExtendWith(MockitoExtension.class)
class SectionCreationServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService;

    private SectionCreationService sectionCreationService;

    @BeforeEach
    void setUp() {
        sectionCreationService = new SectionCreationService(sectionRepository, sectionDocsFilesystemSyncService);
    }

    @Test
    void shouldDefaultDisplayOrderToZeroWhenNotProvided() {
        SectionCreationForm form = new SectionCreationForm();
        form.setName("Introduction");
        form.setDescription("Section racine");

        when(sectionRepository.findByName("Introduction")).thenReturn(Optional.empty());
        when(sectionRepository.save(any(SectionEntity.class))).thenAnswer(invocation -> {
            SectionEntity entity = invocation.getArgument(0);
            entity.setId("section-1");
            return entity;
        });

        Section createdSection = sectionCreationService.createSection(form);

        assertThat(createdSection.getDisplayOrder()).isEqualTo(0);
    }

    @Test
    void shouldThrowWhenDisplayOrderIsNegative() {
        SectionCreationForm form = new SectionCreationForm();
        form.setName("Introduction");
        form.setDescription("Section racine");
        form.setDisplayOrder(-1);

        assertThatThrownBy(() -> sectionCreationService.createSection(form))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("L'ordre d'affichage doit être positif ou nul.");
    }
}