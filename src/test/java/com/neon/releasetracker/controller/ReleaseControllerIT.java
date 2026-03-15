package com.neon.releasetracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.neon.releasetracker.TestcontainersConfiguration;
import com.neon.releasetracker.dto.CreateReleaseRequest;
import com.neon.releasetracker.dto.UpdateReleaseRequest;
import com.neon.releasetracker.model.ReleaseStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext
@Import(TestcontainersConfiguration.class)
class ReleaseControllerIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  // Seed data IDs (inserted in order by DataInitializer on test profile):
  // 1 = Backend 2.1.0   CREATED
  // 2 = Frontend 3.0.0  IN_DEVELOPMENT
  // 3 = Mobile 1.5.0    ON_DEV
  // 4 = Infra 4.0.0     QA_DONE_ON_DEV
  // 5 = Security 1.2.0  ON_STAGING
  // 6 = Auth 2.0.0      DONE

  @Test
  void findAll_returnsPagedReleases() throws Exception {
    mockMvc
        .perform(get("/api/v1/releases"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void findAll_filterByName_returnsMatchingReleases() throws Exception {
    mockMvc
        .perform(get("/api/v1/releases").param("name", "Backend"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].name").value("Backend 2.1.0"));
  }

  @Test
  void findAll_filterByStatus_returnsMatchingReleases() throws Exception {
    mockMvc
        .perform(get("/api/v1/releases").param("status", "CREATED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].status").value("CREATED"));
  }

  @Test
  void findAll_filterByReleaseDateRange_returnsMatchingReleases() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/releases")
                .param("releaseDateFrom", "2025-01-01")
                .param("releaseDateTo", "2025-02-28"))
        .andExpect(status().isOk());
  }

  @Test
  void findById_existingId_returnsRelease() throws Exception {
    mockMvc
        .perform(get("/api/v1/releases/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Backend 2.1.0"))
        .andExpect(jsonPath("$.status").value("CREATED"));
  }

  @Test
  void findById_nonExistingId_returnsNotFound() throws Exception {
    mockMvc.perform(get("/api/v1/releases/999")).andExpect(status().isNotFound());
  }

  @Test
  void create_validRequest_returnsCreated() throws Exception {
    final var request =
        new CreateReleaseRequest("New Release 5.0.0", "Major release", LocalDate.of(2026, 1, 1));

    mockMvc
        .perform(
            post("/api/v1/releases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("New Release 5.0.0"))
        .andExpect(jsonPath("$.status").value("CREATED"));
  }

  @Test
  void create_blankName_returnsBadRequest() throws Exception {
    final var request = new CreateReleaseRequest("", "test", null);

    mockMvc
        .perform(
            post("/api/v1/releases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_withoutReleaseDate_returnsCreated() throws Exception {
    final var request = new CreateReleaseRequest("Dateless Release", "No date", null);

    mockMvc
        .perform(
            post("/api/v1/releases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Dateless Release"))
        .andExpect(jsonPath("$.status").value("CREATED"));
  }

  @Test
  void update_validStatusTransition_returnsOk() throws Exception {
    final var request =
        new UpdateReleaseRequest(
            "Backend 2.1.0",
            "Core API improvements",
            ReleaseStatus.IN_DEVELOPMENT,
            LocalDate.of(2025, 1, 10));

    mockMvc
        .perform(
            put("/api/v1/releases/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("IN_DEVELOPMENT"));
  }

  @Test
  void update_invalidStatusTransition_returnsBadRequest() throws Exception {
    final var request =
        new UpdateReleaseRequest(
            "Backend 2.1.0",
            "Core API improvements",
            ReleaseStatus.DONE,
            LocalDate.of(2025, 1, 10));

    mockMvc
        .perform(
            put("/api/v1/releases/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void update_doneRelease_returnsBadRequest() throws Exception {
    final var request =
        new UpdateReleaseRequest(
            "Auth 2.0.0",
            "Authentication revamp",
            ReleaseStatus.ON_PROD,
            LocalDate.of(2025, 6, 30));

    mockMvc
        .perform(
            put("/api/v1/releases/6")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void update_nonExistingId_returnsNotFound() throws Exception {
    final var request =
        new UpdateReleaseRequest(
            "Unknown", "none", ReleaseStatus.CREATED, LocalDate.of(2025, 1, 1));

    mockMvc
        .perform(
            put("/api/v1/releases/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void delete_existingId_returnsNoContent() throws Exception {
    mockMvc.perform(delete("/api/v1/releases/3")).andExpect(status().isNoContent());
  }

  @Test
  void delete_nonExistingId_returnsNoContent() throws Exception {
    mockMvc.perform(delete("/api/v1/releases/999")).andExpect(status().isNoContent());
  }

  @Test
  void findAll_pagination_returnsCorrectPage() throws Exception {
    mockMvc
        .perform(get("/api/v1/releases").param("page", "1").param("size", "3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(3));
  }
}
