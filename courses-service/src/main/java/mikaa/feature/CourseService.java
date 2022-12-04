package mikaa.feature;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseNameDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.NewCourseDTO;
import mikaa.kafka.EventType;
import mikaa.kafka.KafkaProducer;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService {

  private final KafkaProducer producer;
  private final CourseRepository repository;

  List<CourseSummaryDTO> findAll() {
    return repository.listAll()
        .stream()
        .map(CourseMapper::courseSummary)
        .toList();
  }

  Optional<CourseDTO> findOne(long id) {
    return repository.findByIdOptional(id).map(CourseMapper::course);
  }

  CourseDTO add(NewCourseDTO newCourse) {
    CourseEntity entity = CourseEntity.fromName(newCourse.name());

    newCourse.holes()
        .stream()
        .map(HoleMapper::entity)
        .forEach(entity::addHole);

    repository.persist(entity);

    var savedCourse = CourseMapper.course(entity);
    producer.send(EventType.COURSE_ADDED, savedCourse);

    return savedCourse;
  }

  Optional<CourseNameDTO> updateCourseName(long id, String name) {
    var maybeCourse = repository.findByIdOptional(id)
        .map(course -> {
          course.setName(name);
          return course;
        });

    maybeCourse.ifPresent(repository::persist);

    maybeCourse.map(CourseMapper::course)
        .ifPresent(course -> producer.send(EventType.COURSE_UPDATED, course));

    return maybeCourse.map(CourseMapper::courseName);
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(CourseMapper::course)
        .ifPresent(course -> {
          repository.deleteById(id);
          producer.send(EventType.COURSE_DELETED, course);
        });
  }

}
