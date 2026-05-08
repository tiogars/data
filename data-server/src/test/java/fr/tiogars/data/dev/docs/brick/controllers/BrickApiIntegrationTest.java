package fr.tiogars.data.dev.docs.brick.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import fr.tiogars.data.dev.docs.brick.repositories.BrickRepository;
import fr.tiogars.data.dev.docs.brick.repositories.ExternalLinkRepository;

@SpringBootTest
class BrickApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BrickRepository brickRepository;

    @Autowired
    private ExternalLinkRepository externalLinkRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        brickRepository.deleteAllInBatch();
        externalLinkRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteBrick() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String createPayload = """
            {
              "number": "60284-%s",
              "title": "Le camion de chantier",
                            "tags": ["city", "truck"]
            }
            """.formatted(suffix);

        MvcResult createResult = mockMvc.perform(post("/brick")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.number").value("60284-" + suffix))
            .andExpect(jsonPath("$.title").value("Le camion de chantier"))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/brick"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId))
            .andExpect(jsonPath("$.items[0].number").value("60284-" + suffix));

        String updatePayload = """
            {
              "id": "%s",
              "number": "60284-%s",
              "title": "Camion chantier - edition",
                            "tags": ["city", "updated"]
            }
            """.formatted(createdId, suffix);

        mockMvc.perform(put("/brick/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.title").value("Camion chantier - edition"))
            .andExpect(jsonPath("$.tags[0]").value("city"))
            .andExpect(jsonPath("$.tags[1]").value("updated"));

        mockMvc.perform(get("/brick/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.title").value("Camion chantier - edition"));

        mockMvc.perform(delete("/brick/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/brick/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUpdateAndDeleteExternalLink() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        String createPayload = """
            {
              "name": "BrickLink-%s",
              "url": "https://www.bricklink.com/v2/search.page?q=",
              "enabled": true
            }
            """.formatted(suffix);

        MvcResult createResult = mockMvc.perform(post("/brick/external-link")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("BrickLink-" + suffix))
            .andExpect(jsonPath("$.enabled").value(true))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/brick/external-link"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId));

        String updatePayload = """
            {
              "id": "%s",
              "name": "BrickLink-%s",
              "url": "https://www.bricklink.com/v2/search.page?q=",
              "enabled": false
            }
            """.formatted(createdId, suffix);

        mockMvc.perform(put("/brick/external-link/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.enabled").value(false));

        mockMvc.perform(delete("/brick/external-link/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/brick/external-link/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldImportAndExportBrickState() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String imageDataUrl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA";

        String importPayload = """
            {
              "bricks": [
                {
                  "number": "1000-%s",
                  "title": "Set A",
                                    "tags": ["alpha", "city"],
                                    "imageUrl": "%s"
                },
                {
                  "number": "1001-%s",
                  "title": "Set B",
                                    "tags": ["beta"]
                }
              ],
              "externalLinks": [
                {
                  "name": "Brickset-%s",
                  "url": "https://brickset.com/sets/",
                  "enabled": true
                }
              ]
            }
                        """.formatted(suffix, imageDataUrl, suffix, suffix);

        mockMvc.perform(post("/brick/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(importPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bricks.length()").value(2))
            .andExpect(jsonPath("$.externalLinks.length()").value(1));

        mockMvc.perform(get("/brick/export"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bricks.length()").value(2))
            .andExpect(jsonPath("$.bricks[0].imageBase64").value(imageDataUrl))
            .andExpect(jsonPath("$.externalLinks.length()").value(1))
            .andExpect(jsonPath("$.tags").isArray())
            .andExpect(jsonPath("$.tags.length()").value(3));
    }

    private String extractId(MvcResult result) throws Exception {
        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Matcher matcher = Pattern.compile("\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").matcher(json);

        if (!matcher.find()) {
            throw new IllegalStateException("Impossible d'extraire l'id depuis la reponse: " + json);
        }

        return matcher.group(1);
    }
}
