package mikaa.feature.course;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class CourseRepository implements PanacheRepository<CourseEntity> {

  Optional<CourseEntity> findByExternalId(long externalId) {
    return find("externalId", externalId).firstResultOptional();
  }

}
