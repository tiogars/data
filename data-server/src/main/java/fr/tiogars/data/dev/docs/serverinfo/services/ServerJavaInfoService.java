package fr.tiogars.data.dev.docs.serverinfo.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.serverinfo.models.JavaVersionInfo;
import fr.tiogars.data.dev.docs.serverinfo.models.JpaEntityClassInfoListResponse;
import fr.tiogars.data.dev.docs.serverinfo.repositories.ServerJavaInfoRepository;
import fr.tiogars.data.dev.docs.serverinfo.repositories.ServerJpaEntityInfoRepository;

@Service
public class ServerJavaInfoService {

    private final ServerJavaInfoRepository serverJavaInfoRepository;
    private final ServerJpaEntityInfoRepository serverJpaEntityInfoRepository;

    public ServerJavaInfoService(
        ServerJavaInfoRepository serverJavaInfoRepository,
        ServerJpaEntityInfoRepository serverJpaEntityInfoRepository
    ) {
        this.serverJavaInfoRepository = serverJavaInfoRepository;
        this.serverJpaEntityInfoRepository = serverJpaEntityInfoRepository;
    }

    public JavaVersionInfo getJavaVersionInfo() {
        return serverJavaInfoRepository.getCurrentJavaVersionInfo();
    }

    public JpaEntityClassInfoListResponse listJpaEntityInfos() {
        return new JpaEntityClassInfoListResponse(serverJpaEntityInfoRepository.listJpaEntityInfos());
    }
}
