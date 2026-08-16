package fr.tiogars.data.gateway.routes;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

@Component
public class DomainPathRegistry {

    private final AtomicReference<Set<String>> domainPaths = new AtomicReference<>(Set.of());

    public boolean matches(String requestPath) {
        return domainPaths.get().stream().anyMatch(domainPath ->
                requestPath.equals(domainPath) || requestPath.startsWith(domainPath + "/"));
    }

    public void replace(Set<String> paths) {
        domainPaths.set(Set.copyOf(paths));
    }

    public Set<String> snapshot() {
        return domainPaths.get();
    }
}
