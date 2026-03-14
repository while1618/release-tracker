package com.neon.releasetracker.specification;

import com.neon.releasetracker.dto.ReleaseFilter;
import com.neon.releasetracker.model.Release;
import com.neon.releasetracker.model.ReleaseStatus;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class ReleaseSpecification {

  private ReleaseSpecification() {}

  public static Specification<Release> withFilter(ReleaseFilter filter) {
    return Specification.where(hasName(filter.name()))
        .and(hasStatus(filter.status()))
        .and(releaseDateFrom(filter.releaseDateFrom()))
        .and(releaseDateTo(filter.releaseDateTo()));
  }

  private static Specification<Release> hasName(String name) {
    return (root, query, cb) ->
        name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }

  private static Specification<Release> hasStatus(ReleaseStatus status) {
    return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
  }

  private static Specification<Release> releaseDateFrom(LocalDate from) {
    return (root, query, cb) ->
        from == null ? null : cb.greaterThanOrEqualTo(root.get("releaseDate"), from);
  }

  private static Specification<Release> releaseDateTo(LocalDate to) {
    return (root, query, cb) ->
        to == null ? null : cb.lessThanOrEqualTo(root.get("releaseDate"), to);
  }
}
