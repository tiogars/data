package fr.tiogars.data.dev.docs.serverinfo.services;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import fr.tiogars.data.dev.docs.serverinfo.models.JavaVersionInfo;
import fr.tiogars.data.dev.docs.serverinfo.repositories.ServerJavaInfoRepository;

class ServerJavaInfoServiceTest {

    @Test
    void shouldDelegateToRepository() {
        JavaVersionInfo expected = new JavaVersionInfo();
        expected.setJavaVersion("25");

        ServerJavaInfoRepository repository = new ServerJavaInfoRepositoryStub(expected);
        ServerJavaInfoService service = new ServerJavaInfoService(repository);

        JavaVersionInfo actual = service.getJavaVersionInfo();

        assertSame(expected, actual);
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
}
