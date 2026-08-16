package fr.tiogars.data.dev.docs.serverinfo.services;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

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
        ServerJavaInfoService service = new ServerJavaInfoService(repository, jpaRepository);

        JavaVersionInfo actual = service.getJavaVersionInfo();

        assertSame(expected, actual);
    }

    @Test
    void shouldDelegateJpaEntitiesListingToRepository() {
        JpaEntityClassInfo entityInfo = new JpaEntityClassInfo();
        entityInfo.setSimpleClassName("SectionEntity");

        ServerJavaInfoRepository repository = new ServerJavaInfoRepositoryStub(new JavaVersionInfo());
        ServerJpaEntityInfoRepository jpaRepository = new ServerJpaEntityInfoRepositoryStub(List.of(entityInfo));
        ServerJavaInfoService service = new ServerJavaInfoService(repository, jpaRepository);

        JpaEntityClassInfoListResponse response = service.listJpaEntityInfos();

        assertSame(entityInfo, response.getItems().get(0));
    }

    @Test
    void shouldListStableDomainPaths() {
        ServerJavaInfoService service = new ServerJavaInfoService(
            new ServerJavaInfoRepositoryStub(new JavaVersionInfo()),
            new ServerJpaEntityInfoRepositoryStub(List.of())
        );

        var response = service.listDomainPaths();

        org.junit.jupiter.api.Assertions.assertEquals("/brand", response.getItems().get(0));
        org.junit.jupiter.api.Assertions.assertEquals("/vin-tag", response.getItems().get(response.getCount() - 1));
        org.junit.jupiter.api.Assertions.assertEquals(response.getCount(), response.getItems().stream().distinct().count());
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
