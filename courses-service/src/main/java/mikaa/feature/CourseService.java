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

    maybeCourse.ifPresent(course -> {
      course.setName(name);
      repository.persist(course);
    });

    return maybeCourse.map(CourseMapper::courseName);
  }

  void delete(long id) {
    repository.deleteById(id);
  }

}
