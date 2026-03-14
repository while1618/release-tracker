package com.neon.releasetracker.service.impl;

import com.neon.releasetracker.dto.CreateReleaseRequest;
import com.neon.releasetracker.dto.ReleaseDTO;
import com.neon.releasetracker.dto.ReleaseFilter;
import com.neon.releasetracker.dto.UpdateReleaseRequest;
import com.neon.releasetracker.mapper.ReleaseMapper;
import com.neon.releasetracker.model.Release;
import com.neon.releasetracker.model.ReleaseStatus;
import com.neon.releasetracker.repository.ReleaseRepository;
import com.neon.releasetracker.service.ReleaseService;
import com.neon.releasetracker.error.exception.ReleaseNotFoundException;
import com.neon.releasetracker.specification.ReleaseSpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReleaseServiceImpl implements ReleaseService {

  private final ReleaseRepository releaseRepository;
  private final ReleaseMapper releaseMapper;

  @Override
  public List<ReleaseDTO> findAll(ReleaseFilter filter) {
    return releaseRepository.findAll(ReleaseSpecification.withFilter(filter)).stream()
        .map(releaseMapper::toDTO)
        .toList();
  }

  @Override
  public ReleaseDTO findById(Long id) {
    return releaseRepository
        .findById(id)
        .map(releaseMapper::toDTO)
        .orElseThrow(() -> new ReleaseNotFoundException("release.notFound", id));
  }

  @Override
  @Transactional
  public ReleaseDTO create(CreateReleaseRequest request) {
    Release release = releaseMapper.toEntity(request);
    release.setStatus(ReleaseStatus.CREATED);
    return releaseMapper.toDTO(releaseRepository.save(release));
  }

  @Override
  @Transactional
  public ReleaseDTO update(Long id, UpdateReleaseRequest request) {
    Release release =
        releaseRepository
            .findById(id)
            .orElseThrow(() -> new ReleaseNotFoundException("release.notFound", id));
    releaseMapper.updateEntity(request, release);
    return releaseMapper.toDTO(releaseRepository.save(release));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    releaseRepository.deleteById(id);
  }
}
