package mikaa.feature;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseNameDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.NewCourseDTO;
import mikaa.events.CourseEvents;
import mikaa.kafka.CourseProducer;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService {

  private final CourseProducer producer;
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
    producer.send(CourseEvents.add(savedCourse));

    return savedCourse;
  }

  Optional<CourseNameDTO> updateCourseName(long id, String name) {
    var maybeCourse = repository.findByIdOptional(id);

    maybeCourse.map(course -> {
      course.setName(name);
      return course;
    }).ifPresent(course -> {
      repository.persist(course);
      producer.send(CourseEvents.update(CourseMapper.course(course)));
    });

    return maybeCourse.map(CourseMapper::courseName);
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(CourseMapper::course)
        .map(CourseEvents::delete)
        .ifPresent(event -> {
          repository.deleteById(id);
          producer.send(event);
        });
  }

}
