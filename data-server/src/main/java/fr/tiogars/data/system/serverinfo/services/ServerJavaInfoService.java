package fr.tiogars.data.system.serverinfo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.system.serverinfo.models.DomainPathListResponse;
import fr.tiogars.data.system.serverinfo.models.JavaVersionInfo;
import fr.tiogars.data.system.serverinfo.models.JpaEntityClassInfoListResponse;
import fr.tiogars.data.system.serverinfo.repositories.ServerJavaInfoRepository;
import fr.tiogars.data.system.serverinfo.repositories.ServerJpaEntityInfoRepository;

@Service
public class ServerJavaInfoService {

    private static final List<String> DOMAIN_PATHS = List.of(
        "/brand",
        "/model",
        "/car",
        "/car-mileage",
        "/brick",
        "/section",
        "/section-document",
        "/footer-link",
        "/menu-item",
        "/gtin",
        "/android",
        "/winget",
        "/url-manager",
        "/section-docs-settings",
        "/github-repository",
        "/continent",
        "/github-rest-config",
        "/user-account",
        "/vin",
        "/appellation",
        "/couleur",
        "/circonstance",
        "/contenant",
        "/type-vin",
        "/cepage",
        "/maison",
        "/vin-nom",
        "/vin-tag"
    );

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

    public DomainPathListResponse listDomainPaths() {
        return new DomainPathListResponse(DOMAIN_PATHS);
    }
}
