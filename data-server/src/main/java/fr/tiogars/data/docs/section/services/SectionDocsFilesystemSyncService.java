package fr.tiogars.data.docs.section.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.settings.sectiondocs.entities.SectionDocsSettingEntity;
import fr.tiogars.data.settings.sectiondocs.repositories.SectionDocsSettingRepository;

@Service
public class SectionDocsFilesystemSyncService {

    private final SectionRepository sectionRepository;
    private final SectionDocsSettingRepository sectionDocsSettingRepository;
    private final Path storageRootPath;

    public SectionDocsFilesystemSyncService(
        SectionRepository sectionRepository,
        SectionDocsSettingRepository sectionDocsSettingRepository,
        @Value("${data.docs.storage-root:./volumes/docs}") String storageRootPath
    ) {
        this.sectionRepository = sectionRepository;
        this.sectionDocsSettingRepository = sectionDocsSettingRepository;
        this.storageRootPath = Path.of(storageRootPath).toAbsolutePath().normalize();
    }

    public SectionDocsSyncSnapshot captureSnapshot(String sectionId) {
        List<SectionEntity> sections = sectionRepository.findAll(SectionRepository.DEFAULT_SECTION_SORT);
        Map<String, SectionEntity> sectionsById = toSectionsById(sections);
        SectionEntity section = sectionsById.get(sectionId);
        if (section == null) {
            return SectionDocsSyncSnapshot.empty();
        }

        SectionEntity root = findRoot(section, sectionsById);
        Optional<SectionDocsSettingEntity> setting = sectionDocsSettingRepository.findBySectionId(root.getId());
        if (setting.isEmpty()) {
            return new SectionDocsSyncSnapshot(root.getId(), null);
        }

        SectionTreeNode rootNode = buildSectionTree(root, buildChildrenByParentId(sections));
        return new SectionDocsSyncSnapshot(root.getId(), resolveRootDirectory(setting.get().getStoragePath(), rootNode));
    }

    public Map<String, SectionDocsSyncSnapshot> captureConfiguredRootSnapshots() {
        List<SectionEntity> sections = sectionRepository.findAll(SectionRepository.DEFAULT_SECTION_SORT);
        Map<String, List<SectionEntity>> childrenByParentId = buildChildrenByParentId(sections);
        Map<String, SectionEntity> sectionsById = toSectionsById(sections);
        Map<String, SectionDocsSyncSnapshot> snapshots = new HashMap<>();

        for (SectionDocsSettingEntity setting : sectionDocsSettingRepository.findAll()) {
            SectionEntity root = sectionsById.get(setting.getSectionId());
            if (root == null) {
                continue;
            }
            SectionTreeNode rootNode = buildSectionTree(root, childrenByParentId);
            snapshots.put(root.getId(), new SectionDocsSyncSnapshot(root.getId(), resolveRootDirectory(setting.getStoragePath(), rootNode)));
        }

        return snapshots;
    }

    public void syncAfterSectionCreated(String sectionId) {
        syncSectionChange(sectionId, null);
    }

    public void syncAfterSectionUpdated(String sectionId, SectionDocsSyncSnapshot previousSnapshot) {
        syncSectionChange(sectionId, previousSnapshot);
    }

    public void syncAfterSectionDeleted(SectionDocsSyncSnapshot previousSnapshot) {
        if (previousSnapshot.rootSectionId() != null) {
            syncRootSection(previousSnapshot.rootSectionId(), previousSnapshot.folderPath());
            if (!sectionRepository.existsById(previousSnapshot.rootSectionId())) {
                deleteDirectoryIfExists(previousSnapshot.folderPath());
            }
        }
    }

    public void syncAfterSettingsUpdated(Map<String, SectionDocsSyncSnapshot> previousSnapshots) {
        List<SectionEntity> sections = sectionRepository.findAll(SectionRepository.DEFAULT_SECTION_SORT);
        Map<String, List<SectionEntity>> childrenByParentId = buildChildrenByParentId(sections);
        Map<String, SectionEntity> sectionsById = toSectionsById(sections);
        Map<String, SectionDocsSyncSnapshot> currentSnapshots = new HashMap<>();

        for (SectionDocsSettingEntity setting : sectionDocsSettingRepository.findAll()) {
            SectionEntity root = sectionsById.get(setting.getSectionId());
            if (root == null) {
                continue;
            }

            SectionTreeNode rootNode = buildSectionTree(root, childrenByParentId);
            Path currentFolderPath = resolveRootDirectory(setting.getStoragePath(), rootNode);
            currentSnapshots.put(root.getId(), new SectionDocsSyncSnapshot(root.getId(), currentFolderPath));

            deleteDirectoryIfExists(currentFolderPath);
            writeSectionTree(rootNode, currentFolderPath);
        }

        for (SectionDocsSyncSnapshot previousSnapshot : previousSnapshots.values()) {
            if (previousSnapshot.folderPath() == null) {
                continue;
            }

            SectionDocsSyncSnapshot currentSnapshot = currentSnapshots.get(previousSnapshot.rootSectionId());
            if (currentSnapshot == null || !previousSnapshot.folderPath().equals(currentSnapshot.folderPath())) {
                deleteDirectoryIfExists(previousSnapshot.folderPath());
            }
        }
    }

    private void syncSectionChange(String sectionId, SectionDocsSyncSnapshot previousSnapshot) {
        List<SectionEntity> sections = sectionRepository.findAll(SectionRepository.DEFAULT_SECTION_SORT);
        Map<String, SectionEntity> sectionsById = toSectionsById(sections);
        SectionEntity section = sectionsById.get(sectionId);

        if (section != null) {
            SectionEntity currentRoot = findRoot(section, sectionsById);
            syncRootSection(currentRoot.getId(), previousSnapshot != null ? previousSnapshot.folderPath() : null);

            if (previousSnapshot != null && previousSnapshot.rootSectionId() != null && !previousSnapshot.rootSectionId().equals(currentRoot.getId())) {
                syncRootSection(previousSnapshot.rootSectionId(), previousSnapshot.folderPath());
            }
            return;
        }

        if (previousSnapshot != null && previousSnapshot.rootSectionId() != null) {
            syncRootSection(previousSnapshot.rootSectionId(), previousSnapshot.folderPath());
        }
    }

    private void syncRootSection(String rootSectionId, Path previousFolderPath) {
        Optional<SectionDocsSettingEntity> setting = sectionDocsSettingRepository.findBySectionId(rootSectionId);
        if (setting.isEmpty()) {
            deleteDirectoryIfExists(previousFolderPath);
            return;
        }

        List<SectionEntity> sections = sectionRepository.findAll(SectionRepository.DEFAULT_SECTION_SORT);
        Map<String, SectionEntity> sectionsById = toSectionsById(sections);
        SectionEntity root = sectionsById.get(rootSectionId);
        if (root == null) {
            deleteDirectoryIfExists(previousFolderPath);
            return;
        }

        SectionTreeNode rootNode = buildSectionTree(root, buildChildrenByParentId(sections));
        Path currentFolderPath = resolveRootDirectory(setting.get().getStoragePath(), rootNode);

        deleteDirectoryIfExists(currentFolderPath);
        writeSectionTree(rootNode, currentFolderPath);

        if (previousFolderPath != null && !previousFolderPath.equals(currentFolderPath)) {
            deleteDirectoryIfExists(previousFolderPath);
        }
    }

    private Map<String, SectionEntity> toSectionsById(List<SectionEntity> sections) {
        Map<String, SectionEntity> sectionsById = new HashMap<>();
        for (SectionEntity section : sections) {
            sectionsById.put(section.getId(), section);
        }
        return sectionsById;
    }

    private Map<String, List<SectionEntity>> buildChildrenByParentId(List<SectionEntity> sections) {
        Map<String, List<SectionEntity>> childrenByParentId = new HashMap<>();
        for (SectionEntity section : sections) {
            if (section.getParent() == null) {
                continue;
            }

            childrenByParentId.computeIfAbsent(section.getParent().getId(), key -> new ArrayList<>()).add(section);
        }
        return childrenByParentId;
    }

    private SectionEntity findRoot(SectionEntity section, Map<String, SectionEntity> sectionsById) {
        SectionEntity current = section;
        while (current.getParent() != null) {
            current = sectionsById.getOrDefault(current.getParent().getId(), current.getParent());
        }
        return current;
    }

    private SectionTreeNode buildSectionTree(SectionEntity section, Map<String, List<SectionEntity>> childrenByParentId) {
        List<SectionTreeNode> children = childrenByParentId.getOrDefault(section.getId(), List.of()).stream()
            .sorted(Comparator.comparing((SectionEntity item) -> item.getDisplayOrder() != null ? item.getDisplayOrder() : 0)
                .thenComparing(item -> item.getName() != null ? item.getName() : "", String.CASE_INSENSITIVE_ORDER))
            .map(child -> buildSectionTree(child, childrenByParentId))
            .toList();

        return new SectionTreeNode(
            section.getId(),
            normalizeTitlePart(section.getName(), "Sans nom"),
            normalizeDescription(section.getDescription()),
            section.getDisplayOrder() != null ? section.getDisplayOrder() : 0,
            children
        );
    }

    private Path resolveRootDirectory(String storagePath, SectionTreeNode rootNode) {
        List<Integer> rootIndexSegments = List.of(rootNode.displayOrder());
        return storageRootPath.resolve(storagePath).resolve(buildFolderName(rootIndexSegments, rootNode.title()));
    }

    private void writeSectionTree(SectionTreeNode rootNode, Path rootDirectory) {
        writeSectionTree(rootNode, rootDirectory, new ArrayList<>());
    }

    private void writeSectionTree(SectionTreeNode node, Path directory, List<Integer> indexSegments) {
        List<Integer> nextIndexSegments = new ArrayList<>(indexSegments);
        nextIndexSegments.add(node.displayOrder());

        try {
            Files.createDirectories(directory);
            Files.writeString(
                directory.resolve("index.md"),
                buildMarkdownContent(nextIndexSegments, node.title(), node.description()),
                StandardCharsets.UTF_8
            );

            for (SectionTreeNode child : node.children()) {
                List<Integer> childIndexSegments = new ArrayList<>(nextIndexSegments);
                childIndexSegments.add(child.displayOrder());
                writeSectionTree(child, directory.resolve(buildFolderName(childIndexSegments, child.title())), nextIndexSegments);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de synchroniser la documentation Markdown de la section.", exception);
        }
    }

    private String buildFolderName(List<Integer> indexSegments, String title) {
        return buildIndexString(indexSegments) + "-" + sanitizeTitleForFolder(title);
    }

    private String sanitizeTitleForFolder(String title) {
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFKC)
            .replaceAll("[\\/:*?\"<>|]", "-")
            .replaceAll("\\s+", " ")
            .trim();
        return normalized.isBlank() ? "section" : normalized;
    }

    private String buildMarkdownContent(List<Integer> indexSegments, String title, String description) {
        String index = buildIndexString(indexSegments);

        String normalizedDescription = description == null || description.isBlank()
            ? ""
            : description;

        return "# " + index + "-" + title + System.lineSeparator() + System.lineSeparator() + normalizedDescription;
    }

    private String buildIndexString(List<Integer> indexSegments) {
        return indexSegments.stream()
            .map(String::valueOf)
            .reduce((left, right) -> left + "." + right)
            .orElse("0");
    }

    private String normalizeTitlePart(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }

    private String normalizeDescription(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.trim();
    }

    private void deleteDirectoryIfExists(Path directory) {
        if (directory == null) {
            return;
        }

        try {
            if (!Files.exists(directory)) {
                return;
            }

            try (Stream<Path> paths = Files.walk(directory)) {
                paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException exception) {
                            throw new IllegalStateException("Impossible de supprimer le répertoire documentaire obsolète.", exception);
                        }
                    });
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible d'accéder au répertoire documentaire à supprimer.", exception);
        }
    }

    public record SectionDocsSyncSnapshot(String rootSectionId, Path folderPath) {
        public static SectionDocsSyncSnapshot empty() {
            return new SectionDocsSyncSnapshot(null, null);
        }
    }

    private record SectionTreeNode(
        String id,
        String title,
        String description,
        int displayOrder,
        List<SectionTreeNode> children
    ) {
    }
}