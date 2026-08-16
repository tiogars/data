package fr.tiogars.data.gateway.routes;

import java.io.IOException;
import java.time.Duration;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class DomainPathDiscoveryServiceTest {

    private MockWebServer mockWebServer;

    @AfterEach
    void shutdownServer() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    void shouldReplaceRegistryWhenServerReturnsValidPaths() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"items\":[\"/brand\",\"/car-mileage\"],\"count\":2}"));

        DomainPathRegistry registry = new DomainPathRegistry();
        DomainPathDiscoveryService service = createService(registry);

        service.discoverWithRetries();

        assertThat(registry.snapshot()).containsExactlyInAnyOrder("/brand", "/car-mileage");
    }

    @Test
    void shouldKeepPreviousRegistryWhenServerReturnsInvalidPaths() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"items\":[\"/brand/**\"],\"count\":1}"));

        DomainPathRegistry registry = new DomainPathRegistry();
        registry.replace(java.util.Set.of("/existing"));
        DomainPathDiscoveryService service = createService(registry);

        service.discoverWithRetries();

        assertThat(registry.snapshot()).containsExactly("/existing");
    }

    private DomainPathDiscoveryService createService(DomainPathRegistry registry) {
        return new DomainPathDiscoveryService(
                RestClient.builder(),
                registry,
                "/server-info/domain-paths",
                true,
                1,
                Duration.ZERO,
                mockWebServer.url("/").toString(),
                Duration.ofSeconds(1));
    }
}