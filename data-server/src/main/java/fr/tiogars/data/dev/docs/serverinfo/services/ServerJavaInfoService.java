package fr.tiogars.data.dev.docs.serverinfo.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.serverinfo.models.JavaVersionInfo;
import fr.tiogars.data.dev.docs.serverinfo.repositories.ServerJavaInfoRepository;

@Service
public class ServerJavaInfoService {

    private final ServerJavaInfoRepository serverJavaInfoRepository;

    public ServerJavaInfoService(ServerJavaInfoRepository serverJavaInfoRepository) {
        this.serverJavaInfoRepository = serverJavaInfoRepository;
    }

    public JavaVersionInfo getJavaVersionInfo() {
        return serverJavaInfoRepository.getCurrentJavaVersionInfo();
    }
}
