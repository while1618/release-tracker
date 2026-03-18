package com.neon.releasetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neon.releasetracker.dto.CreateReleaseRequest;
import com.neon.releasetracker.dto.ReleaseDTO;
import com.neon.releasetracker.dto.ReleaseFilter;
import com.neon.releasetracker.dto.UpdateReleaseRequest;
import com.neon.releasetracker.error.exception.InvalidStatusTransitionException;
import com.neon.releasetracker.error.exception.ReleaseNotFoundException;
import com.neon.releasetracker.mapper.ReleaseMapper;
import com.neon.releasetracker.model.Release;
import com.neon.releasetracker.model.ReleaseStatus;
import com.neon.releasetracker.repository.ReleaseRepository;
import com.neon.releasetracker.service.impl.ReleaseServiceImpl;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ReleaseServiceTest {

  @Mock private ReleaseRepository releaseRepository;
  @Mock private ReleaseMapper releaseMapper;

  private ReleaseServiceImpl releaseService;

  private Release release;
  private ReleaseDTO releaseDTO;

  @BeforeEach
  void setUp() {
    releaseService = new ReleaseServiceImpl(releaseRepository, releaseMapper);

    release =
        Release.builder()
            .id(1L)
            .name("1.0.0")
            .description("Initial release")
            .status(ReleaseStatus.CREATED)
            .releaseDate(LocalDate.of(2025, 1, 15))
            .createdAt(Instant.now())
            .lastUpdatedAt(Instant.now())
            .build();

    releaseDTO =
        new ReleaseDTO(
            1L,
            "1.0.0",
            "Initial release",
            ReleaseStatus.CREATED,
            LocalDate.of(2025, 1, 15),
            Instant.now(),
            Instant.now());
  }

  @Test
  void findAll_returnsPageOfDTOs() {
    final var pageable = PageRequest.of(0, 10);
    final var filter = new ReleaseFilter(null, null, null, null);
    final var page = new PageImpl<>(List.of(release));

    when(releaseRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
    when(releaseMapper.toDTO(release)).thenReturn(releaseDTO);

    final var result = releaseService.findAll(filter, pageable);

    assertEquals(1, result.getTotalElements());
    assertEquals(releaseDTO, result.getContent().getFirst());
  }

  @Test
  void findById_existingId_returnsDTO() {
    when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
    when(releaseMapper.toDTO(release)).thenReturn(releaseDTO);

    final var result = releaseService.findById(1L);

    assertEquals(releaseDTO, result);
  }

  @Test
  void findById_nonExistingId_throwsReleaseNotFoundException() {
    when(releaseRepository.findById(99L)).thenReturn(Optional.empty());

    final var ex = assertThrows(ReleaseNotFoundException.class, () -> releaseService.findById(99L));

    assertEquals(99L, ex.getId());
  }

  @Test
  void create_setsCreatedStatusAndSaves() {
    final var request = new CreateReleaseRequest("1.0.0", "Initial release", null);

    when(releaseMapper.toEntity(request)).thenReturn(release);
    when(releaseRepository.save(release)).thenReturn(release);
    when(releaseMapper.toDTO(release)).thenReturn(releaseDTO);

    final var result = releaseService.create(request);

    assertEquals(ReleaseStatus.CREATED, release.getStatus());
    assertEquals(releaseDTO, result);
    verify(releaseRepository).save(release);
  }

  @Test
  void update_sameStatus_savesWithoutTransitionCheck() {
    final var request = new UpdateReleaseRequest("1.0.0", "Updated", ReleaseStatus.CREATED, null);

    when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
    when(releaseRepository.save(release)).thenReturn(release);
    when(releaseMapper.toDTO(release)).thenReturn(releaseDTO);

    final var result = releaseService.update(1L, request);

    assertEquals(releaseDTO, result);
    verify(releaseRepository).save(release);
  }

  @Test
  void update_validForwardTransition_savesRelease() {
    final var request =
        new UpdateReleaseRequest("1.0.0", "Updated", ReleaseStatus.IN_DEVELOPMENT, null);

    when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
    when(releaseRepository.save(release)).thenReturn(release);
    when(releaseMapper.toDTO(release)).thenReturn(releaseDTO);

    releaseService.update(1L, request);

    verify(releaseRepository).save(release);
  }

  @Test
  void update_validBackwardTransition_savesRelease() {
    release.setStatus(ReleaseStatus.IN_DEVELOPMENT);
    final var request = new UpdateReleaseRequest("1.0.0", "Updated", ReleaseStatus.CREATED, null);

    when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
    when(releaseRepository.save(release)).thenReturn(release);
    when(releaseMapper.toDTO(release)).thenReturn(releaseDTO);

    releaseService.update(1L, request);

    verify(releaseRepository).save(release);
  }

  @Test
  void update_invalidTransitionSkipsStep_throwsInvalidStatusTransitionException() {
    final var request = new UpdateReleaseRequest("1.0.0", "Updated", ReleaseStatus.ON_DEV, null);

    when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));

    assertThrows(InvalidStatusTransitionException.class, () -> releaseService.update(1L, request));
  }

  @Test
  void update_transitionFromDone_throwsInvalidStatusTransitionException() {
    release.setStatus(ReleaseStatus.DONE);
    final var request = new UpdateReleaseRequest("1.0.0", "Updated", ReleaseStatus.ON_PROD, null);

    when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));

    assertThrows(InvalidStatusTransitionException.class, () -> releaseService.update(1L, request));
  }

  @Test
  void update_nonExistingId_throwsReleaseNotFoundException() {
    final var request =
        new UpdateReleaseRequest("1.0.0", "Updated", ReleaseStatus.IN_DEVELOPMENT, null);

    when(releaseRepository.findById(99L)).thenReturn(Optional.empty());

    final var ex =
        assertThrows(ReleaseNotFoundException.class, () -> releaseService.update(99L, request));

    assertEquals(99L, ex.getId());
  }

  @Test
  void delete_callsRepositoryDeleteById() {
    releaseService.delete(1L);
    verify(releaseRepository).deleteById(1L);
  }
}
