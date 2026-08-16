package fr.tiogars.data.docs.section.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.forms.SectionCreationForm;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;
import fr.tiogars.data.docs.sectiondocument.repositories.SectionDocumentRepository;

@ExtendWith(MockitoExtension.class)
class SectionCreationServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService;

    @Mock
    private SectionDocumentRepository sectionDocumentRepository;

    @InjectMocks
    private SectionCreationService sectionCreationService;

    @Test
    void shouldDefaultDisplayOrderToZeroWhenNotProvided() {
        SectionCreationForm form = new SectionCreationForm();
        form.setName("Introduction");
        form.setDescription("Section racine");
        form.setDocumentId("doc-1");

        SectionDocumentEntity document = new SectionDocumentEntity();
        document.setId("doc-1");

        when(sectionDocumentRepository.findById("doc-1")).thenReturn(Optional.of(document));
        when(sectionRepository.findByNameAndDocument_Id("Introduction", "doc-1")).thenReturn(Optional.empty());
        when(sectionRepository.save(any(SectionEntity.class))).thenAnswer(invocation -> {
            SectionEntity entity = invocation.getArgument(0);
            entity.setId("section-1");
            return entity;
        });

        Section createdSection = sectionCreationService.createSection(form);

        assertThat(createdSection.getDisplayOrder()).isZero();
    }

    @Test
    void shouldThrowWhenDisplayOrderIsNegative() {
        SectionCreationForm form = new SectionCreationForm();
        form.setName("Introduction");
        form.setDescription("Section racine");
        form.setDocumentId("doc-1");
        form.setDisplayOrder(-1);

        SectionDocumentEntity document = new SectionDocumentEntity();
        document.setId("doc-1");
        when(sectionDocumentRepository.findById("doc-1")).thenReturn(Optional.of(document));

        assertThatThrownBy(() -> sectionCreationService.createSection(form))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("L'ordre d'affichage doit être positif ou nul.");
    }
}