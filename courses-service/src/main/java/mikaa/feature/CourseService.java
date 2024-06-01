package mikaa.feature;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.domain.Course;
import mikaa.domain.CourseSummary;
import mikaa.domain.NewCourse;
import mikaa.producers.CourseProducer;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private final CourseProducer producer;
  private final CourseRepository repository;
  private final CourseValidator validator;

  @Override
  public Optional<CourseEntity> findCourse(long id) {
    return repository.findByIdOptional(id);
  }

  @Override
  public CourseEntity findCourseOrThrow(long id) {
    return findCourse(id).orElseThrow(() -> notFound(id));
  }

  List<CourseSummary> findAll(QueryFilters filters) {
    return repository.streamAll()
        .filter(filters::applyAll)
        .map(CourseService::toSummary)
        .toList();
  }

  Course findByIdOrThrow(long id) {
    return DomainModelMapper.course(findCourseOrThrow(id));
  }

  Course add(NewCourse newCourse) {
    var holes = newCourse.holes()
        .stream()
        .map(hole -> new HoleEntity(hole.number(), hole.par(), hole.distance()))
        .toList();

    var courseEntity = new CourseEntity(newCourse.name(), holes);
    validator.validate(courseEntity);
    courseEntity.getHoles().forEach(h -> h.setCourse(courseEntity)); // For JPA to work correctly
    repository.persist(courseEntity);

    var course = DomainModelMapper.course(courseEntity);
    producer.courseAdded(course);

    return course;
  }

  Course updateCourseName(long id, String name) {
    validator.validate(CourseEntity.fromName(name));

    var courseEntity = findCourseOrThrow(id);
    courseEntity.setName(name);

    var course = DomainModelMapper.course(courseEntity);
    producer.courseUpdated(course);

    return course;
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(DomainModelMapper::course)
        .ifPresent(course -> {
          repository.deleteById(id);
          producer.courseDeleted(course);
        });
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

  private static CourseSummary toSummary(CourseEntity entity) {
    var holeCount = entity.getHoles().size();
    var coursePar = entity.getHoles().stream().mapToInt(HoleEntity::getPar).sum();
    return new CourseSummary(entity.getId(), entity.getName(), holeCount, coursePar);
  }

}
