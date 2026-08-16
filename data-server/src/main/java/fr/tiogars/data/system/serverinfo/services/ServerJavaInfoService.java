package fr.tiogars.data.system.serverinfo.services;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import fr.tiogars.data.system.serverinfo.models.DomainPathListResponse;
import fr.tiogars.data.system.serverinfo.models.JavaVersionInfo;
import fr.tiogars.data.system.serverinfo.models.JpaEntityClassInfoListResponse;
import fr.tiogars.data.system.serverinfo.repositories.ServerJavaInfoRepository;
import fr.tiogars.data.system.serverinfo.repositories.ServerJpaEntityInfoRepository;

@Service
public class ServerJavaInfoService {

    private static final String PATH_SEPARATOR = "/";
    private static final Set<String> TECHNICAL_PATHS = Set.of(
        "/api",
        "/error",
        "/server-info",
        "/swagger-ui.html",
        "/v3"
    );

    private final ServerJavaInfoRepository serverJavaInfoRepository;
    private final ServerJpaEntityInfoRepository serverJpaEntityInfoRepository;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ServerJavaInfoService(
        ServerJavaInfoRepository serverJavaInfoRepository,
        ServerJpaEntityInfoRepository serverJpaEntityInfoRepository,
        RequestMappingHandlerMapping requestMappingHandlerMapping
    ) {
        this.serverJavaInfoRepository = serverJavaInfoRepository;
        this.serverJpaEntityInfoRepository = serverJpaEntityInfoRepository;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    public JavaVersionInfo getJavaVersionInfo() {
        return serverJavaInfoRepository.getCurrentJavaVersionInfo();
    }

    public JpaEntityClassInfoListResponse listJpaEntityInfos() {
        return new JpaEntityClassInfoListResponse(serverJpaEntityInfoRepository.listJpaEntityInfos());
    }

    public DomainPathListResponse listDomainPaths() {
        var domainPaths = new TreeSet<String>();
        for (RequestMappingInfo mapping : requestMappingHandlerMapping.getHandlerMethods().keySet()) {
            var pathPatternsCondition = mapping.getPathPatternsCondition();
            if (pathPatternsCondition == null) {
                continue;
            }
            for (String pattern : pathPatternsCondition.getPatternValues()) {
                String domainPath = extractDomainPath(pattern);
                if (domainPath != null) {
                    domainPaths.add(domainPath);
                }
            }
        }
        return new DomainPathListResponse(List.copyOf(domainPaths));
    }

    private String extractDomainPath(String pattern) {
        String[] segments = pattern.split(PATH_SEPARATOR);
        if (segments.length < 2 || segments[1].isBlank() || segments[1].contains("{")) {
            return null;
        }

        String domainPath = PATH_SEPARATOR + segments[1];
        return TECHNICAL_PATHS.contains(domainPath) ? null : domainPath;
    }
}
