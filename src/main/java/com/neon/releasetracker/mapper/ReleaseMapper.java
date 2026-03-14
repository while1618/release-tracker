package com.neon.releasetracker.mapper;

import com.neon.releasetracker.dto.CreateReleaseRequest;
import com.neon.releasetracker.dto.ReleaseDTO;
import com.neon.releasetracker.dto.UpdateReleaseRequest;
import com.neon.releasetracker.model.Release;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReleaseMapper {

  ReleaseDTO toDTO(Release release);

  Release toEntity(CreateReleaseRequest request);

  void updateEntity(UpdateReleaseRequest request, @MappingTarget Release release);
}
