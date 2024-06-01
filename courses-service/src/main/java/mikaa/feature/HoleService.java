package mikaa.feature;

import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.domain.Hole;
import mikaa.domain.NewHole;
import mikaa.domain.UpdatedHole;
import mikaa.producers.CourseProducer;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseFinder courseFinder;
  private final CourseProducer producer;
  private final HoleRepository repository;

  List<Hole> findHoles(long courseId) {
    var holes = courseFinder.findCourse(courseId)
        .map(CourseEntity::getHoles)
        .orElseGet(Collections::emptyList);

    return holes.stream().map(DomainModelMapper::hole).toList();
  }

  Hole findOne(long courseId, int holeNumber) {
    var course = courseFinder.findCourseOrThrow(courseId);
    var holeEntity = findHoleOrThrow(course, holeNumber);
    return DomainModelMapper.hole(holeEntity);
  }

  Hole add(long courseId, NewHole newHole) {
    var courseEntity = courseFinder.findCourseOrThrow(courseId);
    HoleValidator.validateUniqueHoleNumber(newHole.number(), courseEntity);

    var holeEntity = new HoleEntity(newHole.number(), newHole.par(), newHole.distance());
    courseEntity.addHole(holeEntity);
    repository.persist(holeEntity);

    var course = DomainModelMapper.course(courseEntity);
    producer.courseUpdated(course);

    return DomainModelMapper.hole(holeEntity);
  }

  Hole update(long courseId, int holeNumber, UpdatedHole updatedHole) {
    var courseEntity = courseFinder.findCourseOrThrow(courseId);
    var holeEntity = findHoleOrThrow(courseEntity, holeNumber);

    holeEntity.setDistance(updatedHole.distance());
    holeEntity.setPar(updatedHole.par());

    var hole = DomainModelMapper.hole(holeEntity);
    var course = DomainModelMapper.course(courseEntity);
    course.holes().stream().map(h -> h.id() == hole.id() ? hole : h);

    producer.courseUpdated(course);

    return hole;
  }

  void delete(long courseId, int holeNumber) {
    repository.findByCourseIdAndNumber(courseId, holeNumber)
        .map(hole -> {
          var course = hole.getCourse();
          repository.deleteById(hole.getId());
          repository.flush();
          return course;
        })
        .map(DomainModelMapper::course)
        .ifPresent(producer::courseUpdated);
  }

  private static HoleEntity findHoleOrThrow(CourseEntity course, int holeNumber) {
    return course.findHole(holeNumber).orElseThrow(
        () -> new NotFoundException(String.format("Course %s has no hole %s", course.getId(), holeNumber)));
  }

}
