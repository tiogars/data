package fr.tiogars.data.dev.docs.brand.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.assertj.core.api.Assertions;

import fr.tiogars.data.products.brand.repositories.BrandRepository;

@SpringBootTest
class BrandApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        brandRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteBrand() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String createPayload = """
            {
              "name": "Lego-%s",
              "description": "Description initiale"
            }
            """.formatted(suffix);

        MvcResult createResult = mockMvc.perform(post("/brand")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Lego-" + suffix))
            .andExpect(jsonPath("$.description").value("Description initiale"))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/brand"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId))
            .andExpect(jsonPath("$.items[0].name").value("Lego-" + suffix));

        String updatePayload = """
            {
              "id": "%s",
              "name": "Lego-%s-updated",
              "description": "Description mise a jour"
            }
            """.formatted(createdId, suffix);

        mockMvc.perform(put("/brand/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Lego-" + suffix + "-updated"))
            .andExpect(jsonPath("$.description").value("Description mise a jour"));

        mockMvc.perform(get("/brand/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Lego-" + suffix + "-updated"));

        mockMvc.perform(delete("/brand/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/brand/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldImportAndExportBrands() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

                String existingBrandPayload = """
                        {
                            "name": "Alpha-%s",
                            "description": "Deja present"
                        }
                        """.formatted(suffix);

                mockMvc.perform(post("/brand")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(existingBrandPayload))
                        .andExpect(status().isOk());

        String importPayload = """
            {
                            "text": "Alpha-%s\\nBeta-%s\\nBeta-%s\\n\\nGamma-%s"
            }
                        """.formatted(suffix, suffix, suffix, suffix);

        mockMvc.perform(post("/brand/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(importPayload))
            .andExpect(status().isOk())
                        .andExpect(jsonPath("$.addedCount").value(2))
                        .andExpect(jsonPath("$.notAddedCount").value(2))
                        .andExpect(jsonPath("$.alreadyExistsCount").value(2))
                        .andExpect(jsonPath("$.invalidCount").value(0));

        mockMvc.perform(get("/brand/export"))
            .andExpect(status().isOk())
                        .andExpect(jsonPath("$.count").value(3))
                        .andExpect(jsonPath("$.items[0].name").isNotEmpty())
                        .andExpect(jsonPath("$.items[1].name").isNotEmpty())
                        .andExpect(jsonPath("$.items[2].name").isNotEmpty());

        MvcResult exportCsvResult = mockMvc.perform(get("/brand/export/csv"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"brand-export.csv\""))
            .andReturn();

        String exportedCsv = exportCsvResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertThat(exportedCsv).contains("name,description");
    }

    @Test
    void shouldImportBrandsFromCsv() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        String csvPayload = "name,description\nCsv-A-%s,Desc A\nCsv-B-%s,Desc B\nCsv-A-%s,Duplicate\n"
            .formatted(suffix, suffix, suffix);

        mockMvc.perform(post("/brand/import/csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .content(csvPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.addedCount").value(2))
            .andExpect(jsonPath("$.notAddedCount").value(1));

        mockMvc.perform(get("/brand/export"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2));
    }

        @Test
        void shouldImportBrandsFromLegacyJsonFormat() throws Exception {
                String suffix = UUID.randomUUID().toString().substring(0, 8);

                String importPayload = """
                        {
                            "items": [
                                {
                                    "name": "Legacy-A-%s",
                                    "description": "A"
                                },
                                {
                                    "name": "Legacy-B-%s",
                                    "description": "B"
                                },
                                {
                                    "name": "Legacy-A-%s",
                                    "description": "A duplicate"
                                }
                            ]
                        }
                        """.formatted(suffix, suffix, suffix);

                mockMvc.perform(post("/brand/import")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(importPayload))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.importedCount").value(2))
                        .andExpect(jsonPath("$.skippedCount").value(1))
                        .andExpect(jsonPath("$.addedCount").value(2))
                        .andExpect(jsonPath("$.notAddedCount").value(1))
                        .andExpect(jsonPath("$.duplicateNames[0]").value("Legacy-A-" + suffix));

                mockMvc.perform(get("/brand/export"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.count").value(2));
        }

    @Test
    void shouldDeleteAllBrands() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        String firstBrandPayload = """
            {
              "name": "First-%s",
              "description": "First"
            }
            """.formatted(suffix);

        String secondBrandPayload = """
            {
              "name": "Second-%s",
              "description": "Second"
            }
            """.formatted(suffix);

        mockMvc.perform(post("/brand")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstBrandPayload))
            .andExpect(status().isOk());

        mockMvc.perform(post("/brand")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondBrandPayload))
            .andExpect(status().isOk());

        mockMvc.perform(get("/brand"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2));

        mockMvc.perform(delete("/brand"))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/brand"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(0));
    }

    private static String extractId(MvcResult result) throws Exception {
        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(response);
        if (!matcher.find()) {
            throw new IllegalStateException("Impossible de trouver l'id dans la reponse: " + response);
        }
        return matcher.group(1);
    }
}
