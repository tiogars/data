package fr.tiogars.data.dev.docs.serverinfo.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import fr.tiogars.data.system.serverinfo.models.JavaVersionInfo;
import fr.tiogars.data.system.serverinfo.models.JpaEntityClassInfo;
import fr.tiogars.data.system.serverinfo.models.JpaEntityClassInfoListResponse;
import fr.tiogars.data.system.serverinfo.repositories.ServerJavaInfoRepository;
import fr.tiogars.data.system.serverinfo.repositories.ServerJpaEntityInfoRepository;
import fr.tiogars.data.system.serverinfo.services.ServerJavaInfoService;

class ServerJavaInfoServiceTest {

    @Test
    void shouldDelegateToRepository() {
        JavaVersionInfo expected = new JavaVersionInfo();
        expected.setJavaVersion("25");

        ServerJavaInfoRepository repository = new ServerJavaInfoRepositoryStub(expected);
        ServerJpaEntityInfoRepository jpaRepository = new ServerJpaEntityInfoRepositoryStub(List.of());
        ServerJavaInfoService service = new ServerJavaInfoService(repository, jpaRepository, handlerMapping());

        JavaVersionInfo actual = service.getJavaVersionInfo();

        assertSame(expected, actual);
    }

    @Test
    void shouldDelegateJpaEntitiesListingToRepository() {
        JpaEntityClassInfo entityInfo = new JpaEntityClassInfo();
        entityInfo.setSimpleClassName("SectionEntity");

        ServerJavaInfoRepository repository = new ServerJavaInfoRepositoryStub(new JavaVersionInfo());
        ServerJpaEntityInfoRepository jpaRepository = new ServerJpaEntityInfoRepositoryStub(List.of(entityInfo));
        ServerJavaInfoService service = new ServerJavaInfoService(repository, jpaRepository, handlerMapping());

        JpaEntityClassInfoListResponse response = service.listJpaEntityInfos();

        assertSame(entityInfo, response.getItems().get(0));
    }

    @Test
    void shouldListDomainPathsFromControllerMappings() {
        RequestMappingHandlerMapping handlerMapping = handlerMapping(
            "/vin-tag/search",
            "/brand",
            "/brand/{id}",
            "/api/v1/sync/{domain}/changes",
            "/server-info/domain-paths",
            "/error",
            "/swagger-ui.html",
            "/v3/api-docs"
        );
        ServerJavaInfoService service = new ServerJavaInfoService(
            new ServerJavaInfoRepositoryStub(new JavaVersionInfo()),
            new ServerJpaEntityInfoRepositoryStub(List.of()),
            handlerMapping
        );

        var response = service.listDomainPaths();

        org.junit.jupiter.api.Assertions.assertEquals(List.of("/brand", "/vin-tag"), response.getItems());
    }

    private static RequestMappingHandlerMapping handlerMapping(String... patterns) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = new LinkedHashMap<>();
        for (String pattern : patterns) {
            handlerMethods.put(RequestMappingInfo.paths(pattern).build(), mock(HandlerMethod.class));
        }
        RequestMappingHandlerMapping handlerMapping = mock(RequestMappingHandlerMapping.class);
        when(handlerMapping.getHandlerMethods()).thenReturn(handlerMethods);
        return handlerMapping;
    }

    private static class ServerJavaInfoRepositoryStub extends ServerJavaInfoRepository {
        private final JavaVersionInfo fixed;

        private ServerJavaInfoRepositoryStub(JavaVersionInfo fixed) {
            this.fixed = fixed;
        }

        @Override
        public JavaVersionInfo getCurrentJavaVersionInfo() {
            return fixed;
        }
    }

    private static class ServerJpaEntityInfoRepositoryStub extends ServerJpaEntityInfoRepository {
        private final List<JpaEntityClassInfo> fixed;

        private ServerJpaEntityInfoRepositoryStub(List<JpaEntityClassInfo> fixed) {
            this.fixed = fixed;
        }

        @Override
        public List<JpaEntityClassInfo> listJpaEntityInfos() {
            return fixed;
        }
    }
}
