package fr.tiogars.data.settings.footerlink.services;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.tiogars.data.settings.footerlink.entities.FooterLinkEntity;
import fr.tiogars.data.settings.footerlink.repositories.FooterLinkRepository;

@Component
public class FooterLinkDefaultDataInitializer implements CommandLineRunner {

    private final FooterLinkRepository footerLinkRepository;

    public FooterLinkDefaultDataInitializer(FooterLinkRepository footerLinkRepository) {
        this.footerLinkRepository = footerLinkRepository;
    }

    @Override
    public void run(String... args) {
        if (footerLinkRepository.count() > 0) {
            return;
        }

        footerLinkRepository.saveAll(List.of(
            createFooterLink("React", "https://react.dev/", "react", 10),
            createFooterLink("MUI", "https://mui.com/", "mui", 20),
            createFooterLink("Redux Toolkit", "https://redux-toolkit.js.org/", "redux", 30),
            createFooterLink("Vite", "https://vitejs.dev/", "vite", 40),
            createFooterLink("TypeScript", "https://www.typescriptlang.org/", "typescript", 50),
            createFooterLink("Java", "https://adoptium.net/", "java", 60),
            createFooterLink("Spring Boot", "https://spring.io/projects/spring-boot", "spring", 70),
            createFooterLink("GitHub", "https://github.com/tiogars/data", "github", 80)
        ));
    }

    private FooterLinkEntity createFooterLink(String label, String url, String icon, int displayOrder) {
        FooterLinkEntity entity = new FooterLinkEntity();
        entity.setLabel(label);
        entity.setUrl(url);
        entity.setIcon(icon);
        entity.setDisplayOrder(displayOrder);
        return entity;
    }
}