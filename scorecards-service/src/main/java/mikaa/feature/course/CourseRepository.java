package mikaa.feature.course;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CourseRepository implements PanacheRepository<CourseEntity> {
}
