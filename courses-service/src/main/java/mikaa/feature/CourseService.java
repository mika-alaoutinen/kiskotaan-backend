package mikaa.feature;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kafka.courses.CourseProducer;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private final CourseProducer producer;
  private final CourseRepository repository;
  private final CourseValidator validator;

  List<CourseSummary> findAll() {
    return repository.listAll()
        .stream()
        .map(CourseSummary::from)
        .toList();
  }

  @Override
  public CourseEntity findOne(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  CourseEntity add(CourseEntity newCourse) {
    validator.validate(newCourse);
    repository.persist(newCourse);
    producer.courseAdded(CourseMapper.course(newCourse));
    return newCourse;
  }

  CourseEntity updateCourseName(long id, String name) {
    var course = repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
    validator.validate(CourseEntity.fromName(name));

    course.setName(name);
    producer.courseUpdated(CourseMapper.course(course));

    return course;
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(CourseMapper::course)
        .ifPresent(course -> {
          repository.deleteById(id);
          producer.courseDeleted(course);
        });
  }

  private static NotFoundException notFound(long id) {
    String msg = "Could not find course with id " + id;
    return new NotFoundException(msg);
  }

}
