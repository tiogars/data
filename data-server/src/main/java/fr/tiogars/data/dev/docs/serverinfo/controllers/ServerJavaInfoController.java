package fr.tiogars.data.dev.docs.serverinfo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.data.dev.docs.serverinfo.models.JavaVersionInfo;
import fr.tiogars.data.dev.docs.serverinfo.models.JpaEntityClassInfoListResponse;
import fr.tiogars.data.dev.docs.serverinfo.services.ServerJavaInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "server-info", description = "Informations techniques exposees par le serveur.")
public class ServerJavaInfoController {

    private final ServerJavaInfoService serverJavaInfoService;

    public ServerJavaInfoController(ServerJavaInfoService serverJavaInfoService) {
        this.serverJavaInfoService = serverJavaInfoService;
    }

    @GetMapping("/server-info/java-version")
    @Operation(summary = "Lire la version Java du serveur", description = "Retourne la version Java utilisee par la JVM active du serveur.")
    public ResponseEntity<JavaVersionInfo> getJavaVersion() {
        return ResponseEntity.ok(serverJavaInfoService.getJavaVersionInfo());
    }

    @GetMapping("/server-info/jpa-entities")
    @Operation(
        summary = "Lister les entites JPA du serveur",
        description = "Retourne les classes annotees @Entity avec leurs attributs et principales metadonnees jakarta.persistence."
    )
    public ResponseEntity<JpaEntityClassInfoListResponse> listJpaEntities() {
        return ResponseEntity.ok(serverJavaInfoService.listJpaEntityInfos());
    }
}
