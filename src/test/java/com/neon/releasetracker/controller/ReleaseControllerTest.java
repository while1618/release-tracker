package com.neon.releasetracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.neon.releasetracker.dto.ReleaseDTO;
import com.neon.releasetracker.error.exception.InvalidStatusTransitionException;
import com.neon.releasetracker.error.exception.ReleaseNotFoundException;
import com.neon.releasetracker.interceptor.RequestInterceptor;
import com.neon.releasetracker.logger.CustomLogger;
import com.neon.releasetracker.model.ReleaseStatus;
import com.neon.releasetracker.service.ErrorService;
import com.neon.releasetracker.service.ReleaseService;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReleaseController.class)
class ReleaseControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ReleaseService releaseService;
  @MockitoBean private CustomLogger customLogger;
  @MockitoBean private ErrorService errorService;
  @MockitoBean private RequestInterceptor requestInterceptor;

  private ReleaseDTO releaseDTO;

  @BeforeEach
  void setUp() {
    releaseDTO =
        new ReleaseDTO(
            1L,
            "1.0.0",
            "Initial release",
            ReleaseStatus.CREATED,
            LocalDate.of(2025, 1, 15),
            Instant.now(),
            Instant.now());

    when(requestInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void findAll_returnsOk() throws Exception {
    when(releaseService.findAll(any(), any())).thenReturn(new PageImpl<>(List.of(releaseDTO)));

    mockMvc
        .perform(get("/api/v1/releases"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(1L))
        .andExpect(jsonPath("$.content[0].name").value("1.0.0"));
  }

  @Test
  void findById_existingId_returnsOk() throws Exception {
    when(releaseService.findById(1L)).thenReturn(releaseDTO);

    mockMvc
        .perform(get("/api/v1/releases/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Release 1.0.0"));
  }

  @Test
  void findById_nonExistingId_returnsNotFound() throws Exception {
    when(releaseService.findById(99L)).thenThrow(new ReleaseNotFoundException(99L));
    when(errorService.getMessage(anyString(), any())).thenReturn("Release with id 99 not found.");

    mockMvc.perform(get("/api/v1/releases/99")).andExpect(status().isNotFound());
  }

  @Test
  void create_validRequest_returnsCreated() throws Exception {
    when(releaseService.create(any())).thenReturn(releaseDTO);

    mockMvc
        .perform(
            post("/api/v1/releases")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"1.0.0\",\"description\":\"Initial release\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("1.0.0"));
  }

  @Test
  void create_blankName_returnsBadRequest() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/releases")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void update_validRequest_returnsOk() throws Exception {
    when(releaseService.update(any(), any())).thenReturn(releaseDTO);

    mockMvc
        .perform(
            put("/api/v1/releases/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"1.0.0\",\"status\":\"In Development\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  void update_invalidStatusTransition_returnsBadRequest() throws Exception {
    when(releaseService.update(any(), any()))
        .thenThrow(new InvalidStatusTransitionException("Created", "Done"));
    when(errorService.getMessage(anyString(), any()))
        .thenReturn("Cannot transition status from 'Created' to 'Done'.");

    mockMvc
        .perform(
            put("/api/v1/releases/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"1.0.0\",\"status\":\"Done\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void update_nonExistingId_returnsNotFound() throws Exception {
    when(releaseService.update(any(), any())).thenThrow(new ReleaseNotFoundException(99L));
    when(errorService.getMessage(anyString(), any())).thenReturn("Release with id 99 not found.");

    mockMvc
        .perform(
            put("/api/v1/releases/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"1.0.0\",\"status\":\"In Development\"}"))
        .andExpect(status().isNotFound());
  }

  @Test
  void delete_existingId_returnsNoContent() throws Exception {
    doNothing().when(releaseService).delete(1L);

    mockMvc.perform(delete("/api/v1/releases/1")).andExpect(status().isNoContent());
  }
}
