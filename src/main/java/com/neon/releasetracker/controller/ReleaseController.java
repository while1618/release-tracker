package com.neon.releasetracker.controller;

import com.neon.releasetracker.dto.CreateReleaseRequest;
import com.neon.releasetracker.dto.ReleaseDTO;
import com.neon.releasetracker.dto.ReleaseFilter;
import com.neon.releasetracker.dto.UpdateReleaseRequest;
import com.neon.releasetracker.model.ReleaseStatus;
import com.neon.releasetracker.service.ReleaseService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
public class ReleaseController {

  private final ReleaseService releaseService;

  @GetMapping
  public ResponseEntity<List<ReleaseDTO>> findAll(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) ReleaseStatus status,
      @RequestParam(required = false) LocalDate releaseDateFrom,
      @RequestParam(required = false) LocalDate releaseDateTo) {
    return ResponseEntity.ok(
        releaseService.findAll(new ReleaseFilter(name, status, releaseDateFrom, releaseDateTo)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReleaseDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(releaseService.findById(id));
  }

  @PostMapping
  public ResponseEntity<ReleaseDTO> create(@RequestBody @Valid CreateReleaseRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(releaseService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReleaseDTO> update(
      @PathVariable Long id, @RequestBody @Valid UpdateReleaseRequest request) {
    return ResponseEntity.ok(releaseService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    releaseService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
