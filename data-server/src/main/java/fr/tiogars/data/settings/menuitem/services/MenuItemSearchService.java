package fr.tiogars.data.settings.menuitem.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.tiogars.data.settings.menuitem.entities.MenuItemEntity;
import fr.tiogars.data.settings.menuitem.models.MenuItem;
import fr.tiogars.data.settings.menuitem.models.MenuItemSearchResponse;
import fr.tiogars.data.settings.menuitem.repositories.MenuItemRepository;

@Service
public class MenuItemSearchService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemSearchService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItemSearchResponse searchMenuItems(int page, int size, String query) {
        String normalizedQuery = normalizeQuery(query);

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.asc("label"))
        );

        Page<MenuItemEntity> result = menuItemRepository.findAll(createSearchSpecification(normalizedQuery), pageable);

        List<MenuItem> items = result.getContent().stream()
            .map(MenuItemModelMapper::toMenuItemModel)
            .toList();

        return new MenuItemSearchResponse(items, toSafeCount(result.getTotalElements()), page, size, normalizedQuery);
    }

    private Specification<MenuItemEntity> createSearchSpecification(String query) {
        if (query == null) {
            return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String likePattern = "%" + query.toLowerCase() + "%";

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("label")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("path"), "")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("icon"), "")), likePattern)
        );
    }

    private String normalizeQuery(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }
        return query.trim();
    }

    private int toSafeCount(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }
}
