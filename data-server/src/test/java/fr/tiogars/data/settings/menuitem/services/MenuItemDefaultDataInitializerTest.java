package fr.tiogars.data.settings.menuitem.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.data.settings.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.settings.menuitem.repositories.MenuItemRepository;

@ExtendWith(MockitoExtension.class)
class MenuItemDefaultDataInitializerTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    private final MenuGroupDefinitions menuGroupDefinitions = new MenuGroupDefinitions();

    @Test
    void shouldAddIconGalleryEntryWhenInitializingDefaultMenuItems() {
        when(menuItemRepository.findByLabel(anyString())).thenReturn(Optional.empty());
        when(menuItemRepository.findByPath(anyString())).thenReturn(Optional.empty());
        when(menuItemRepository.save(any(MenuItemEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        MenuItemDefaultDataInitializer initializer = new MenuItemDefaultDataInitializer(
            menuItemRepository,
            menuGroupDefinitions,
            List.of()
        );

        initializer.run();

        ArgumentCaptor<MenuItemEntity> savedItemsCaptor = ArgumentCaptor.forClass(MenuItemEntity.class);
        verify(menuItemRepository, atLeastOnce()).save(savedItemsCaptor.capture());

        assertThat(savedItemsCaptor.getAllValues())
            .anySatisfy(item -> {
                assertThat(item.getLabel()).isEqualTo("Galerie icônes");
                assertThat(item.getPath()).isEqualTo("/icon-gallery");
                assertThat(item.getDisplayOrder()).isEqualTo(22);
                assertThat(item.getDefaultLoaded()).isTrue();
                assertThat(item.getParent()).isNotNull();
                assertThat(item.getParent().getLabel()).isEqualTo("Interface");
            });
    }
}
