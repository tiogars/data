package fr.tiogars.data.dev.docs.serverinfo.services;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.tiogars.data.dev.docs.serverinfo.models.JavaVersionInfo;
import fr.tiogars.data.dev.docs.serverinfo.models.JpaEntityClassInfo;
import fr.tiogars.data.dev.docs.serverinfo.models.JpaEntityClassInfoListResponse;
import fr.tiogars.data.dev.docs.serverinfo.repositories.ServerJavaInfoRepository;
import fr.tiogars.data.dev.docs.serverinfo.repositories.ServerJpaEntityInfoRepository;

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
