package fr.tiogars.data.gateway.routes;

import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainPathRegistryTest {

    @Test
    void shouldMatchDomainRootAndSubPathsButNotSimilarPaths() {
        DomainPathRegistry registry = new DomainPathRegistry();
        registry.replace(Set.of("/brand", "/car-mileage"));

        assertThat(registry.matches("/brand")).isTrue();
        assertThat(registry.matches("/brand/123")).isTrue();
        assertThat(registry.matches("/car-mileage/table")).isTrue();
        assertThat(registry.matches("/branding")).isFalse();
        assertThat(registry.matches("/car")).isFalse();
    }

    @Test
    void shouldReplaceSnapshotAtomically() {
        DomainPathRegistry registry = new DomainPathRegistry();
        registry.replace(Set.of("/brand"));

        registry.replace(Set.of("/model"));

        assertThat(registry.snapshot()).containsExactly("/model");
        assertThat(registry.matches("/brand")).isFalse();
    }
}
