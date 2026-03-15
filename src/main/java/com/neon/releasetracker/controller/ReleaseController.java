package com.neon.releasetracker.controller;

import com.neon.releasetracker.dto.CreateReleaseRequest;
import com.neon.releasetracker.dto.ReleaseDTO;
import com.neon.releasetracker.dto.ReleaseFilter;
import com.neon.releasetracker.dto.UpdateReleaseRequest;
import com.neon.releasetracker.model.ReleaseStatus;
import com.neon.releasetracker.service.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/releases")
@RequiredArgsConstructor
@Tag(name = "Releases", description = "Manage software releases and their lifecycle")
public class ReleaseController {

  private final ReleaseService releaseService;

  @GetMapping
  @Operation(
      summary = "Get all releases",
      description =
          "Returns a paginated list of releases, optionally filtered by name, status, or release date range")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Releases retrieved successfully")
  })
  public ResponseEntity<Page<ReleaseDTO>> findAll(
      @Parameter(description = "Filter by name (partial match)") @RequestParam(required = false)
          String name,
      @Parameter(description = "Filter by status") @RequestParam(required = false)
          ReleaseStatus status,
      @Parameter(description = "Filter by release date from (inclusive), format: yyyy-MM-dd")
          @RequestParam(required = false)
          LocalDate releaseDateFrom,
      @Parameter(description = "Filter by release date to (inclusive), format: yyyy-MM-dd")
          @RequestParam(required = false)
          LocalDate releaseDateTo,
      Pageable pageable) {
    return ResponseEntity.ok(
        releaseService.findAll(
            new ReleaseFilter(name, status, releaseDateFrom, releaseDateTo), pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get release by ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Release found"),
    @ApiResponse(responseCode = "404", description = "Release not found")
  })
  public ResponseEntity<ReleaseDTO> findById(
      @Parameter(description = "Release ID") @PathVariable Long id) {
    return ResponseEntity.ok(releaseService.findById(id));
  }

  @PostMapping
  @Operation(
      summary = "Create a release",
      description = "Creates a new release with status CREATED")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Release created"),
    @ApiResponse(responseCode = "400", description = "Invalid request body")
  })
  public ResponseEntity<ReleaseDTO> create(@RequestBody @Valid CreateReleaseRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(releaseService.create(request));
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a release",
      description =
          "Updates a release. Status must transition one step at a time; DONE is terminal")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Release updated"),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request or invalid status transition"),
    @ApiResponse(responseCode = "404", description = "Release not found")
  })
  public ResponseEntity<ReleaseDTO> update(
      @Parameter(description = "Release ID") @PathVariable Long id,
      @RequestBody @Valid UpdateReleaseRequest request) {
    return ResponseEntity.ok(releaseService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete a release",
      description =
          "Deletes a release by ID. Idempotent — returns 204 even if the release does not exist")
  @ApiResponse(responseCode = "204", description = "Release deleted")
  public ResponseEntity<Void> delete(@Parameter(description = "Release ID") @PathVariable Long id) {
    releaseService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
