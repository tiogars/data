package fr.tiogars.data.dev.docs.serverinfo.repositories;

import org.springframework.stereotype.Repository;

import fr.tiogars.data.dev.docs.serverinfo.models.JavaVersionInfo;

@Repository
public class ServerJavaInfoRepository {

    public JavaVersionInfo getCurrentJavaVersionInfo() {
        JavaVersionInfo info = new JavaVersionInfo();
        info.setJavaVersion(System.getProperty("java.version"));
        info.setRuntimeVersion(System.getProperty("java.runtime.version"));
        info.setVmName(System.getProperty("java.vm.name"));
        info.setVmVendor(System.getProperty("java.vm.vendor"));
        info.setOsName(System.getProperty("os.name"));
        return info;
    }
}
