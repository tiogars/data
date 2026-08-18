package fr.tiogars.data.dev.githubrestconfig.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.githubrestconfig.models.GitHubTokenPermission;
import fr.tiogars.data.dev.githubrestconfig.models.GitHubTokenPermissionResponse;

@Service
public class GitHubTokenPermissionService {

    private static final Map<String, OperationDefinition> OPERATION_CATALOG = createOperationCatalog();

    public GitHubTokenPermissionResponse resolveRequiredPermissions(List<String> operations) {
        if (operations == null || operations.isEmpty()) {
            throw new IllegalArgumentException("La liste des opérations est obligatoire.");
        }

        LinkedHashSet<String> normalizedOperations = new LinkedHashSet<>();
        LinkedHashSet<String> unknownOperations = new LinkedHashSet<>();
        LinkedHashMap<String, PermissionAccumulator> permissionByName = new LinkedHashMap<>();

        for (String rawOperation : operations) {
            String normalized = normalizeOperation(rawOperation);
            if (normalized == null) {
                continue;
            }

            OperationDefinition definition = OPERATION_CATALOG.get(normalized);
            if (definition == null) {
                unknownOperations.add(normalized);
                continue;
            }

            normalizedOperations.add(definition.code);
            for (PermissionRequirement requirement : definition.requirements) {
                permissionByName.compute(requirement.permission, (permission, current) -> mergeRequirement(current, requirement));
            }
        }

        if (normalizedOperations.isEmpty() && unknownOperations.isEmpty()) {
            throw new IllegalArgumentException("Aucune opération valide n'a été fournie.");
        }

        List<GitHubTokenPermission> requiredPermissions = permissionByName.values().stream()
            .map(GitHubTokenPermissionService::toModel)
            .toList();

        return new GitHubTokenPermissionResponse(
            new ArrayList<>(normalizedOperations),
            new ArrayList<>(unknownOperations),
            requiredPermissions
        );
    }

    private static GitHubTokenPermission toModel(@NonNull PermissionAccumulator accumulator) {
        return accumulator.toModel();
    }

    private PermissionAccumulator mergeRequirement(PermissionAccumulator current, PermissionRequirement incoming) {
        if (current == null) {
            return new PermissionAccumulator(incoming.permission, incoming.access, incoming.reason);
        }

        AccessLevel maxAccess = current.accessLevel.max(incoming.access);
        String reason = current.reason;
        if (incoming.access.isStrongerThan(current.accessLevel)) {
            reason = incoming.reason;
        }

        return new PermissionAccumulator(incoming.permission, maxAccess, reason);
    }

    private static String normalizeOperation(String operation) {
        if (operation == null || operation.isBlank()) {
            return null;
        }

        return operation.trim()
            .toLowerCase()
            .replace('_', '-')
            .replace("pull.requests", "pull-requests");
    }

    private static Map<String, OperationDefinition> createOperationCatalog() {
        Map<String, OperationDefinition> catalog = new LinkedHashMap<>();

        register(catalog, "repository.read", List.of(
            requirement("Metadata", AccessLevel.READ, "Nécessaire pour identifier un repository ciblé."),
            requirement("Contents", AccessLevel.READ, "Nécessaire pour lire les contenus et fichiers du repository.")
        ));

        register(catalog, "repository.write", List.of(
            requirement("Metadata", AccessLevel.READ, "Nécessaire pour identifier un repository ciblé."),
            requirement("Contents", AccessLevel.WRITE, "Nécessaire pour créer, modifier ou supprimer des fichiers.")
        ));

        register(catalog, "issues.read", List.of(
            requirement("Issues", AccessLevel.READ, "Nécessaire pour consulter les issues.")
        ));

        register(catalog, "issues.write", List.of(
            requirement("Issues", AccessLevel.WRITE, "Nécessaire pour créer ou modifier des issues.")
        ));

        register(catalog, "pull-requests.read", List.of(
            requirement("Pull requests", AccessLevel.READ, "Nécessaire pour consulter les pull requests.")
        ));

        register(catalog, "pull-requests.write", List.of(
            requirement("Pull requests", AccessLevel.WRITE, "Nécessaire pour créer, commenter ou mettre à jour des pull requests.")
        ));

        register(catalog, "actions.read", List.of(
            requirement("Actions", AccessLevel.READ, "Nécessaire pour consulter les workflows et runs GitHub Actions.")
        ));

        register(catalog, "actions.write", List.of(
            requirement("Actions", AccessLevel.WRITE, "Nécessaire pour relancer, annuler ou gérer des workflows.")
        ));

        register(catalog, "webhooks.read", List.of(
            requirement("Webhooks", AccessLevel.READ, "Nécessaire pour consulter la configuration des webhooks.")
        ));

        register(catalog, "webhooks.write", List.of(
            requirement("Webhooks", AccessLevel.WRITE, "Nécessaire pour créer, modifier ou supprimer des webhooks.")
        ));

        return catalog;
    }

    private static void register(Map<String, OperationDefinition> catalog, String code, List<PermissionRequirement> requirements) {
        catalog.put(code, new OperationDefinition(code, requirements));
    }

    private static PermissionRequirement requirement(String permission, AccessLevel access, String reason) {
        return new PermissionRequirement(permission, access, reason);
    }

    private record OperationDefinition(String code, List<PermissionRequirement> requirements) {
    }

    private record PermissionRequirement(String permission, AccessLevel access, String reason) {
    }

    private static class PermissionAccumulator {

        private final String permission;
        private final AccessLevel accessLevel;
        private final String reason;

        private PermissionAccumulator(String permission, AccessLevel accessLevel, String reason) {
            this.permission = permission;
            this.accessLevel = accessLevel;
            this.reason = reason;
        }

        private GitHubTokenPermission toModel() {
            GitHubTokenPermission model = new GitHubTokenPermission();
            model.setPermission(permission);
            model.setAccess(accessLevel.label);
            model.setReason(reason);
            return model;
        }
    }

    private enum AccessLevel {
        READ("read"),
        WRITE("write");

        private final String label;

        AccessLevel(String label) {
            this.label = label;
        }

        private boolean isStrongerThan(AccessLevel other) {
            return this.ordinal() > other.ordinal();
        }

        private AccessLevel max(AccessLevel other) {
            return isStrongerThan(other) ? this : other;
        }
    }
}
