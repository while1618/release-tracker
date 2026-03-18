package com.neon.releasetracker.service.impl;

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
import com.neon.releasetracker.service.ReleaseService;
import com.neon.releasetracker.specification.ReleaseSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReleaseServiceImpl implements ReleaseService {

  private final ReleaseRepository releaseRepository;
  private final ReleaseMapper releaseMapper;

  @Override
  public Page<ReleaseDTO> findAll(ReleaseFilter filter, Pageable pageable) {
    log.info("Fetching releases with filter: " + filter);
    return releaseRepository
        .findAll(ReleaseSpecification.withFilter(filter), pageable)
        .map(releaseMapper::toDTO);
  }

  @Override
  public ReleaseDTO findById(Long id) {
    log.info("Fetching release with id: " + id);
    return releaseRepository
        .findById(id)
        .map(releaseMapper::toDTO)
        .orElseThrow(
            () -> {
              var ex = new ReleaseNotFoundException(id);
              log.error("Release not found with id: " + id, ex);
              return ex;
            });
  }

  @Override
  @Transactional
  public ReleaseDTO create(CreateReleaseRequest request) {
    log.info("Creating release: " + request.name());
    Release release = releaseMapper.toEntity(request);
    release.setStatus(ReleaseStatus.CREATED);
    ReleaseDTO created = releaseMapper.toDTO(releaseRepository.save(release));
    log.info("Release created with id: " + created.id());
    return created;
  }

  @Override
  @Transactional
  public ReleaseDTO update(Long id, UpdateReleaseRequest request) {
    log.info("Updating release with id: " + id);
    Release release =
        releaseRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  var ex = new ReleaseNotFoundException(id);
                  log.error("Release not found with id: " + id, ex);
                  return ex;
                });
    if (release.getStatus() != request.status()
        && !release.getStatus().canTransitionTo(request.status())) {
      var ex =
          new InvalidStatusTransitionException(release.getStatus().name(), request.status().name());
      log.error(
          "Invalid status transition from " + release.getStatus() + " to " + request.status(), ex);
      throw ex;
    }
    releaseMapper.updateEntity(request, release);
    ReleaseDTO updated = releaseMapper.toDTO(releaseRepository.save(release));
    log.info("Release updated with id: " + id);
    return updated;
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deleting release with id: " + id);
    releaseRepository.deleteById(id);
    log.info("Release deleted with id: " + id);
  }
}
