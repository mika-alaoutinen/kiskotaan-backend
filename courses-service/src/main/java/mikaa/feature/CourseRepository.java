package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class CourseRepository implements PanacheRepository<CourseEntity> {

  boolean existsByName(String name) {
    return count("name", name) > 0;
  }

}
