package mikaa.feature.course;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.domain.Course;
import mikaa.domain.CourseSummary;
import mikaa.domain.Hole;
import mikaa.domain.NewCourse;
import mikaa.feature.hole.HoleEntity;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.producers.courses.CourseProducer;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private static final ModelMapper MAPPER = new ModelMapper();
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
    return toCourse(findCourseOrThrow(id));
  }

  Course add(NewCourse newCourse) {
    var holes = newCourse.holes()
        .stream()
        .map(hole -> new HoleEntity(hole.number(), hole.par(), hole.distance()))
        .toList();

    var course = new CourseEntity(newCourse.name(), holes);
    validator.validate(course);
    course.getHoles().forEach(h -> h.setCourse(course)); // For JPA to work correctly

    repository.persist(course);
    producer.courseAdded(toPayload(course));

    return toCourse(course);
  }

  Course updateCourseName(long id, String name) {
    var course = findCourseOrThrow(id);
    validator.validate(CourseEntity.fromName(name));

    course.setName(name);
    producer.courseUpdated(CourseService.toPayload(course));

    return toCourse(course);
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(CourseService::toPayload)
        .ifPresent(payload -> {
          repository.deleteById(id);
          producer.courseDeleted(payload);
        });
  }

  private static NotFoundException notFound(long id) {
    String msg = "Could not find course with id " + id;
    return new NotFoundException(msg);
  }

  private static Course toCourse(CourseEntity entity) {
    var holes = entity.getHoles()
        .stream()
        .map(hole -> new Hole(hole.getId(), hole.getNumber(), hole.getPar(), hole.getDistance()))
        .toList();

    return new Course(entity.getId(), entity.getName(), holes);
  }

  private static CourseSummary toSummary(CourseEntity entity) {
    var holeCount = entity.getHoles().size();
    var coursePar = entity.getHoles().stream().mapToInt(HoleEntity::getPar).sum();
    return new CourseSummary(entity.getId(), entity.getName(), holeCount, coursePar);
  }

  private static CoursePayload toPayload(CourseEntity entity) {
    return MAPPER.map(entity, CoursePayload.class);
  }

}
