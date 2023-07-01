package mikaa.feature;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CourseUpdated;
import mikaa.producers.courses.CourseProducer;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private final CourseProducer producer;
  private final CourseRepository repository;
  private final CourseValidator validator;

  List<CourseSummary> findAll(QueryFilters filters) {
    return repository.streamAll()
        .filter(filters::applyAll)
        .map(CourseSummary::from)
        .toList();
  }

  @Override
  public Optional<CourseEntity> findCourse(long id) {
    return repository.findByIdOptional(id);
  }

  CourseEntity findCourseOrThrow(long id) {
    return findCourse(id).orElseThrow(() -> notFound(id));
  }

  CourseEntity add(CourseEntity newCourse) {
    validator.validate(newCourse);
    newCourse.getHoles().forEach(h -> h.setCourse(newCourse)); // For JPA to work correctly
    repository.persist(newCourse);
    producer.courseAdded(CourseMapper.toPayload(newCourse));
    return newCourse;
  }

  CourseEntity updateCourseName(long id, String name) {
    var course = repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
    validator.validate(CourseEntity.fromName(name));

    course.setName(name);
    producer.courseUpdated(new CourseUpdated(course.getId(), name));

    return course;
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(CourseMapper::toPayload)
        .ifPresent(payload -> {
          repository.deleteById(id);
          producer.courseDeleted(payload);
        });
  }

  private static NotFoundException notFound(long id) {
    String msg = "Could not find course with id " + id;
    return new NotFoundException(msg);
  }

}
