package mikaa.feature;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseNameDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.NewCourseDTO;
import mikaa.kafka.courses.CourseEventType;
import mikaa.kafka.courses.CourseProducer;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService {

  private final CourseProducer producer;
  private final CourseRepository repository;
  private final CourseValidator validator;

  List<CourseSummaryDTO> findAll() {
    return repository.listAll()
        .stream()
        .map(CourseMapper::courseSummary)
        .toList();
  }

  CourseDTO findOne(long id) {
    return repository.findByIdOptional(id)
        .map(CourseMapper::course)
        .orElseThrow(() -> notFound(id));
  }

  CourseDTO add(NewCourseDTO newCourse) {
    CourseEntity entity = CourseEntity.fromName(newCourse.name());

    validator.validate(entity);

    newCourse.holes()
        .stream()
        .map(HoleMapper::entity)
        .forEach(entity::addHole);

    repository.persist(entity);

    var savedCourse = CourseMapper.course(entity);
    producer.send(CourseEventType.COURSE_ADDED, savedCourse);

    return savedCourse;
  }

  CourseNameDTO updateCourseName(long id, String name) {
    var course = repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
    validator.validate(CourseEntity.fromName(name));

    course.setName(name);
    producer.send(CourseEventType.COURSE_UPDATED, CourseMapper.course(course));

    return CourseMapper.courseName(course);
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(CourseMapper::course)
        .ifPresent(course -> {
          repository.deleteById(id);
          producer.send(CourseEventType.COURSE_DELETED, course);
        });
  }

  private static NotFoundException notFound(long id) {
    String msg = "Could not find course with id " + id;
    return new NotFoundException(msg);
  }

}
