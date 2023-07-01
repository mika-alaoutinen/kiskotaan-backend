package mikaa.feature.hole;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class HoleRepository implements PanacheRepository<HoleEntity> {

  Optional<HoleEntity> findByCourseIdAndNumber(long courseId, int number) {
    return find("course.id = ?1 and number = ?2", courseId, number).firstResultOptional();
  }

}
