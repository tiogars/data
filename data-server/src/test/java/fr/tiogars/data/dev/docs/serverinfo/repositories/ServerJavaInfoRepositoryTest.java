package fr.tiogars.data.dev.docs.serverinfo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import fr.tiogars.data.dev.docs.serverinfo.models.JavaVersionInfo;

class ServerJavaInfoRepositoryTest {

    @Test
    void shouldReadCurrentJvmSystemProperties() {
        ServerJavaInfoRepository repository = new ServerJavaInfoRepository();

        JavaVersionInfo info = repository.getCurrentJavaVersionInfo();

        assertNotNull(info);
        assertEquals(System.getProperty("java.version"), info.getJavaVersion());
        assertEquals(System.getProperty("java.runtime.version"), info.getRuntimeVersion());
        assertEquals(System.getProperty("java.vm.name"), info.getVmName());
        assertEquals(System.getProperty("java.vm.vendor"), info.getVmVendor());
        assertEquals(System.getProperty("os.name"), info.getOsName());
    }
}
