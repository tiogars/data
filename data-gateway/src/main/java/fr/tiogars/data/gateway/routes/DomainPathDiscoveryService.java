package fr.tiogars.data.gateway.routes;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DomainPathDiscoveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainPathDiscoveryService.class);

    private final RestClient restClient;
    private final DomainPathRegistry registry;
    private final String discoveryPath;
    private final boolean enabled;
    private final int maxAttempts;
    private final Duration retryDelay;

    public DomainPathDiscoveryService(
            RestClient.Builder restClientBuilder,
            DomainPathRegistry registry,
            @org.springframework.beans.factory.annotation.Value("${data.gateway.domain-discovery.path:/server-info/domain-paths}") String discoveryPath,
            @org.springframework.beans.factory.annotation.Value("${data.gateway.domain-discovery.enabled:true}") boolean enabled,
            @org.springframework.beans.factory.annotation.Value("${data.gateway.domain-discovery.max-attempts:5}") int maxAttempts,
            @org.springframework.beans.factory.annotation.Value("${data.gateway.domain-discovery.retry-delay:PT2S}") Duration retryDelay,
            @org.springframework.beans.factory.annotation.Value("${data.gateway.downstream-base-url}") String downstreamBaseUrl,
            @org.springframework.beans.factory.annotation.Value("${data.gateway.domain-discovery.timeout:PT2S}") Duration timeout
    ) {
        var requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(timeout);
        this.restClient = restClientBuilder
                .requestFactory(requestFactory)
                .baseUrl(downstreamBaseUrl)
                .build();
        this.registry = registry;
        this.discoveryPath = discoveryPath;
        this.enabled = enabled;
        this.maxAttempts = maxAttempts;
        this.retryDelay = retryDelay;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void discoverAfterStartup() {
        if (!enabled) {
            LOGGER.info("Domain path discovery is disabled");
            return;
        }
        Thread.ofVirtual().name("domain-path-discovery").start(this::discoverWithRetries);
    }

    void discoverWithRetries() {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                DomainPathDiscoveryResponse response = restClient.get()
                        .uri(discoveryPath)
                        .retrieve()
                        .body(DomainPathDiscoveryResponse.class);
                Set<String> paths = validate(response);
                registry.replace(paths);
                LOGGER.info("Loaded {} domain paths from data-server", paths.size());
                return;
            } catch (RuntimeException _) {
                LOGGER.warn("Unable to load domain paths from data-server (attempt {}/{})", attempt, maxAttempts);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException _) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    private Set<String> validate(DomainPathDiscoveryResponse response) {
        if (response == null || response.items() == null || response.items().isEmpty()) {
            throw new IllegalArgumentException("Domain path response is empty");
        }

        Set<String> paths = new LinkedHashSet<>();
        for (String path : response.items()) {
            if (!isValidDomainPath(path)) {
                throw new IllegalArgumentException("Invalid domain path");
            }
            paths.add(path);
        }
        if (paths.size() != response.items().size()) {
            throw new IllegalArgumentException("Duplicate domain path");
        }
        return paths;
    }

    private boolean isValidDomainPath(String path) {
        if (path == null || !path.startsWith("/") || path.length() == 1) {
            return false;
        }
        String segment = path.substring(1);
        for (int index = 0; index < segment.length(); index++) {
            char character = segment.charAt(index);
            if (!(character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9')
                    || (character == '-' && (index == 0 || index == segment.length() - 1))) {
                return false;
            }
        }
        return true;
    }
}
