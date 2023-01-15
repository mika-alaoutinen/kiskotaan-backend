package mikaa.feature.course;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class CourseRepository implements PanacheRepository<CourseEntity> {
}
