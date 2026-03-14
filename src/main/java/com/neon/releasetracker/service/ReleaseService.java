package com.neon.releasetracker.service;

import com.neon.releasetracker.dto.CreateReleaseRequest;
import com.neon.releasetracker.dto.ReleaseDTO;
import com.neon.releasetracker.dto.ReleaseFilter;
import com.neon.releasetracker.dto.UpdateReleaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReleaseService {

  Page<ReleaseDTO> findAll(ReleaseFilter filter, Pageable pageable);

  ReleaseDTO findById(Long id);

  ReleaseDTO create(CreateReleaseRequest request);

  ReleaseDTO update(Long id, UpdateReleaseRequest request);

  void delete(Long id);
}
