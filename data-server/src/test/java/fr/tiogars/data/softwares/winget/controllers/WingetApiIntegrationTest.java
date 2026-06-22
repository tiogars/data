package fr.tiogars.data.softwares.winget.controllers;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.tiogars.data.softwares.winget.repositories.WingetRepository;

@SpringBootTest
class WingetApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WingetRepository wingetRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        wingetRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateSearchAndDeleteWingetApp() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String wingetId = "Notepad++.Notepad++-" + suffix;

        String createPayload = """
            {
              "name": "Notepad++ %s",
              "description": "Editor Windows",
              "wingetId": "%s",
              "installCommand": "winget install -e --id %s",
              "tags": ["editor", "windows"]
            }
            """.formatted(suffix, wingetId, wingetId);

        MvcResult createResult = mockMvc.perform(post("/winget")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.wingetId").value(wingetId))
            .andExpect(jsonPath("$.tags.length()").value(2))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/winget/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId));

        mockMvc.perform(get("/winget/search").param("q", "Notepad"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1));

        String updatePayload = """
            {
              "id": "%s",
              "name": "Notepad++ %s Updated",
              "description": "Editor Windows mis a jour",
              "wingetId": "%s",
              "installCommand": "winget install --id %s",
              "tags": ["editor"]
            }
            """.formatted(createdId, suffix, wingetId, wingetId);

        mockMvc.perform(put("/winget/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Notepad++ " + suffix + " Updated"))
            .andExpect(jsonPath("$.tags.length()").value(1));

        mockMvc.perform(get("/winget/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId));

        mockMvc.perform(delete("/winget/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/winget/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectDuplicateWingetIdOnCreation() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String wingetId = "Git.Git-" + suffix;

        String payload = """
            {
              "name": "Git %s",
              "wingetId": "%s",
              "installCommand": "winget install -e --id %s"
            }
            """.formatted(suffix, wingetId, wingetId);

        mockMvc.perform(post("/winget")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk());

        mockMvc.perform(post("/winget")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldImportWingetsFromMultilineTextAndSkipDuplicates() throws Exception {
        String importPayload = """
            {
              "wingetIdsText": "Microsoft.VisualStudioCode\\nNotepad++.Notepad++\\nMicrosoft.VisualStudioCode"
            }
            """;

        mockMvc.perform(post("/winget/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(importPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.createdCount").value(2))
            .andExpect(jsonPath("$.skippedCount").value(1))
            .andExpect(jsonPath("$.createdItems.length()").value(2))
            .andExpect(jsonPath("$.skippedWingetIds.length()").value(1))
            .andExpect(jsonPath("$.skippedWingetIds[0]").value("Microsoft.VisualStudioCode"))
            .andExpect(jsonPath("$.createdItems[0].name").value("VisualStudioCode"))
            .andExpect(jsonPath("$.createdItems[0].installCommand").value("winget install -e --id Microsoft.VisualStudioCode"));

        mockMvc.perform(get("/winget/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2));
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
