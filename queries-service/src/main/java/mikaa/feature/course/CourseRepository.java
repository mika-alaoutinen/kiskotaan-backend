package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import mikaa.feature.KiskotaanRepository;

@ApplicationScoped
class CourseRepository extends KiskotaanRepository<CourseEntity> {
}
