package com.neon.releasetracker.service;

import com.neon.releasetracker.dto.CreateReleaseRequest;
import com.neon.releasetracker.dto.ReleaseDTO;
import com.neon.releasetracker.dto.ReleaseFilter;
import com.neon.releasetracker.dto.UpdateReleaseRequest;
import java.util.List;

public interface ReleaseService {

  List<ReleaseDTO> findAll(ReleaseFilter filter);

  ReleaseDTO findById(Long id);

  ReleaseDTO create(CreateReleaseRequest request);

  ReleaseDTO update(Long id, UpdateReleaseRequest request);

  void delete(Long id);
}
