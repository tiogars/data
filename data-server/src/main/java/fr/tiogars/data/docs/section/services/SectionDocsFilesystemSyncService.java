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
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;
import fr.tiogars.data.docs.sectiondocument.repositories.SectionDocumentRepository;

@Service
public class SectionDocsFilesystemSyncService {

    private final SectionRepository sectionRepository;
    private final SectionDocumentRepository sectionDocumentRepository;
    private final Path storageRootPath;

    public SectionDocsFilesystemSyncService(
        SectionRepository sectionRepository,
        SectionDocumentRepository sectionDocumentRepository,
        @Value("${data.docs.storage-root:./volumes/docs}") String storageRootPath
    ) {
        this.sectionRepository = sectionRepository;
        this.sectionDocumentRepository = sectionDocumentRepository;
        this.storageRootPath = Path.of(storageRootPath).toAbsolutePath().normalize();
    }

    public SectionDocsSyncSnapshot captureSnapshot(String sectionId) {
        SectionEntity section = sectionRepository.findById(sectionId).orElse(null);
        if (section == null || section.getDocument() == null) {
            return SectionDocsSyncSnapshot.empty();
        }

        return new SectionDocsSyncSnapshot(
            section.getDocument().getId(),
            resolveDocumentDirectory(section.getDocument().getStoragePath())
        );
    }

    public Map<String, SectionDocsSyncSnapshot> captureConfiguredRootSnapshots() {
        Map<String, SectionDocsSyncSnapshot> snapshots = new HashMap<>();

        for (SectionDocumentEntity document : sectionDocumentRepository.findAll()) {
            snapshots.put(
                document.getId(),
                new SectionDocsSyncSnapshot(document.getId(), resolveDocumentDirectory(document.getStoragePath()))
            );
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
        if (previousSnapshot != null && previousSnapshot.rootSectionId() != null) {
            syncDocumentById(previousSnapshot.rootSectionId());
        }
    }

    public void syncAfterSettingsUpdated(Map<String, SectionDocsSyncSnapshot> previousSnapshots) {
        Map<String, SectionDocsSyncSnapshot> currentSnapshots = captureConfiguredRootSnapshots();

        for (SectionDocumentEntity document : sectionDocumentRepository.findAll()) {
            syncDocumentById(document.getId());
        }

        for (SectionDocsSyncSnapshot previousSnapshot : previousSnapshots.values()) {
            if (previousSnapshot == null || previousSnapshot.rootSectionId() == null || previousSnapshot.folderPath() == null) {
                continue;
            }

            if (!currentSnapshots.containsKey(previousSnapshot.rootSectionId())) {
                deleteDirectoryIfExists(previousSnapshot.folderPath());
            }
        }
    }

    public void syncDocumentById(String documentId) {
        SectionDocumentEntity document = sectionDocumentRepository.findById(documentId).orElse(null);
        if (document == null) {
            return;
        }

        List<SectionEntity> sections = sectionRepository.findAllByDocument_Id(documentId, SectionRepository.DEFAULT_SECTION_SORT);
        Map<String, List<SectionEntity>> childrenByParentId = buildChildrenByParentId(sections);
        List<SectionEntity> roots = sections.stream()
            .filter(section -> section.getParent() == null)
            .sorted(sectionComparator())
            .toList();

        Path documentDirectory = resolveDocumentDirectory(document.getStoragePath());
        deleteDirectoryIfExists(documentDirectory);

        try {
            Files.createDirectories(documentDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de créer le répertoire du document.", exception);
        }

        for (SectionEntity root : roots) {
            SectionTreeNode rootNode = buildSectionTree(root, childrenByParentId);
            Path rootDirectory = documentDirectory.resolve(buildFolderName(List.of(rootNode.displayOrder()), rootNode.title()));
            writeSectionTree(rootNode, rootDirectory, List.of());
        }
    }

    private void syncSectionChange(String sectionId, SectionDocsSyncSnapshot previousSnapshot) {
        SectionEntity section = sectionRepository.findById(sectionId).orElse(null);

        if (section != null && section.getDocument() != null) {
            syncDocumentById(section.getDocument().getId());
        }

        if (previousSnapshot != null && previousSnapshot.rootSectionId() != null) {
            syncDocumentById(previousSnapshot.rootSectionId());
        }
    }

    private Path resolveDocumentDirectory(String storagePath) {
        String normalized = storagePath == null ? "" : storagePath.trim().replace('\\', '/');
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        if (normalized.isBlank()) {
            return storageRootPath.resolve("default");
        }

        return storageRootPath.resolve(normalized).normalize();
    }

    private Map<String, List<SectionEntity>> buildChildrenByParentId(List<SectionEntity> sections) {
        Map<String, List<SectionEntity>> childrenByParentId = new HashMap<>();

        for (SectionEntity section : sections) {
            if (section.getParent() == null) {
                continue;
            }

            childrenByParentId.computeIfAbsent(section.getParent().getId(), key -> new ArrayList<>()).add(section);
        }

        for (List<SectionEntity> children : childrenByParentId.values()) {
            children.sort(sectionComparator());
        }

        return childrenByParentId;
    }

    private Comparator<SectionEntity> sectionComparator() {
        return Comparator.comparing((SectionEntity item) -> item.getDisplayOrder() != null ? item.getDisplayOrder() : 0)
            .thenComparing(item -> item.getName() != null ? item.getName() : "", String.CASE_INSENSITIVE_ORDER);
    }

    private SectionTreeNode buildSectionTree(SectionEntity section, Map<String, List<SectionEntity>> childrenByParentId) {
        List<SectionTreeNode> children = childrenByParentId.getOrDefault(section.getId(), List.of()).stream()
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
        String normalizedDescription = description == null || description.isBlank() ? "" : description;

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
