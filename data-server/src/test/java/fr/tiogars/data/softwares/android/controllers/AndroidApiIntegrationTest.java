package fr.tiogars.data.softwares.android.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.tiogars.data.softwares.android.repositories.AndroidRepository;

@SpringBootTest
class AndroidApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AndroidRepository androidRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        androidRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteAndroid() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String createPayload = """
            {
              "name": "Application-%s",
              "packageName": "com.example.%s",
              "category": ["productivity", "notes"],
              "description": "Description initiale",
              "icon": "data:image/png;base64,AAAA"
            }
            """.formatted(suffix, suffix);

        MvcResult createResult = mockMvc.perform(post("/android")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Application-" + suffix))
            .andExpect(jsonPath("$.packageName").value("com.example." + suffix))
            .andExpect(jsonPath("$.category.length()").value(2))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/android"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId))
            .andExpect(jsonPath("$.items[0].packageName").value("com.example." + suffix));

        String updatePayload = """
            {
              "id": "%s",
              "name": "Application-%s-updated",
              "packageName": "com.example.%s.updated",
              "category": ["productivity"],
              "description": "Description mise a jour",
              "icon": "data:image/png;base64,BBBB"
            }
            """.formatted(createdId, suffix, suffix);

        mockMvc.perform(put("/android/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Application-" + suffix + "-updated"))
            .andExpect(jsonPath("$.packageName").value("com.example." + suffix + ".updated"))
            .andExpect(jsonPath("$.category.length()").value(1));

        mockMvc.perform(get("/android/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Application-" + suffix + "-updated"));

        mockMvc.perform(delete("/android/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/android/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldImportExportAndPrintAndroids() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String importPayload = """
            {
              "items": [
                {
                  "name": "App-%s",
                  "packageName": "com.example.%s",
                  "category": ["security", "productivity"],
                  "description": "A"
                },
                {
                  "name": "App-%s",
                  "packageName": "com.example.%s.duplicate",
                  "category": ["utilities"],
                  "description": "B"
                },
                {
                  "name": "App-%s-duplicate",
                  "packageName": "com.example.%s",
                  "category": ["security"],
                  "description": "Duplicate package"
                }
              ]
            }
            """.formatted(suffix, suffix, suffix, suffix, suffix, suffix);

        mockMvc.perform(post("/android/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(importPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.importedCount").value(2))
            .andExpect(jsonPath("$.skippedCount").value(1))
            .andExpect(jsonPath("$.duplicatePackageNames[0]").value("com.example." + suffix));

        mockMvc.perform(get("/android/export"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.items[0].category.length()").value(2));

        mockMvc.perform(get("/android/print").param("mode", "all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.total").value(2))
            .andExpect(jsonPath("$.generatedAt").isNotEmpty());

        mockMvc.perform(get("/android/print")
                .param("mode", "filtered")
                .param("packageName", "duplicate"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.total").value(2));

        String csvPayload = """
            name,packageName,category,description,icon
            AppCsv-%s,com.example.csv.%s,security|utilities,CSV description,
            AppCsv-%s,com.example.csv.%s.duplicate,notes,CSV duplicate,
            AppCsv-%s-dup,com.example.csv.%s,productivity,Duplicate package,
            """.formatted(suffix, suffix, suffix, suffix, suffix, suffix);

        mockMvc.perform(post("/android/import/csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .content(csvPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.importedCount").value(2))
            .andExpect(jsonPath("$.skippedCount").value(1));

        MvcResult exportCsvResult = mockMvc.perform(get("/android/export/csv"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"android-export.csv\""))
            .andReturn();

        String exportedCsv = exportCsvResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(exportedCsv).contains("name,packageName,category,description,icon");
        assertThat(exportedCsv).contains("security|utilities");
    }

    @Test
    void shouldDeleteAllAndroids() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        String firstPayload = """
            {
              "name": "First-%s",
              "packageName": "com.example.first.%s"
            }
            """.formatted(suffix, suffix);

        String secondPayload = """
            {
              "name": "Second-%s",
              "packageName": "com.example.second.%s"
            }
            """.formatted(suffix, suffix);

        mockMvc.perform(post("/android")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstPayload))
            .andExpect(status().isOk());

        mockMvc.perform(post("/android")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondPayload))
            .andExpect(status().isOk());

        mockMvc.perform(get("/android"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2));

        mockMvc.perform(delete("/android"))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/android"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(0));
    }

    private static String extractId(MvcResult result) throws Exception {
        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");
        Matcher matcher = pattern.matcher(response);
        if (!matcher.find()) {
            throw new IllegalStateException("Impossible de trouver l'id dans la reponse: " + response);
        }
        return matcher.group(1);
    }
}