package fr.tiogars.data.dev.docs.githubrestconfig.services;

import fr.tiogars.data.dev.docs.githubrestconfig.entities.GitHubRestConfigEntity;
import fr.tiogars.data.dev.docs.githubrestconfig.models.GitHubRestConfig;

final class GitHubRestConfigModelMapper {

    private GitHubRestConfigModelMapper() {
    }

    static GitHubRestConfig toModel(GitHubRestConfigEntity entity) {
        GitHubRestConfig model = new GitHubRestConfig();
        model.setId(entity.getId());
        model.setIdentifier(entity.getIdentifier());
        model.setTokenPreview(maskToken(entity.getToken()));
        model.setComment(entity.getComment());
        return model;
    }

    private static String maskToken(String token) {
        if (token == null || token.isBlank()) {
            return "****";
        }

        String trimmed = token.trim();
        if (trimmed.length() <= 8) {
            return "****";
        }

        return trimmed.substring(0, 4) + "****" + trimmed.substring(trimmed.length() - 4);
    }
}
