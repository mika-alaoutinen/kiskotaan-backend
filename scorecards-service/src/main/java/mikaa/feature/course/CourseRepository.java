package mikaa.feature.course;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class CourseRepository implements PanacheRepository<CourseEntity> {

  void deleteByExternalId(long externalId) {
    findByExternalId(externalId).ifPresent(this::delete);
  }

  Optional<CourseEntity> findByExternalId(long externalId) {
    return find("externalId", externalId).firstResultOptional();
  }

}
