package mikaa.feature;

import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.HolePayload;
import mikaa.producers.holes.HoleProducer;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseFinder courseFinder;
  private final HoleProducer producer;
  private final HoleRepository repository;

  List<HoleEntity> findHoles(long courseId) {
    return courseFinder.findCourse(courseId)
        .map(CourseEntity::getHoles)
        .orElseGet(Collections::emptyList);
  }

  HoleEntity findOne(long courseId, int holeNumber) {
    var course = courseFinder.findCourseOrThrow(courseId);
    return findHoleOrThrow(course, holeNumber);
  }

  HoleEntity add(long courseId, HoleEntity newHole) {
    var course = courseFinder.findCourseOrThrow(courseId);

    HoleValidator.validateUniqueHoleNumber(newHole.getHoleNumber(), course);
    course.addHole(newHole);

    repository.persist(newHole);
    producer.holeAdded(payload(newHole));

    return newHole;
  }

  HoleEntity update(long courseId, int holeNumber, HoleEntity updatedHole) {
    var course = courseFinder.findCourseOrThrow(courseId);
    var hole = findHoleOrThrow(course, holeNumber);

    hole.setDistance(updatedHole.getDistance());
    hole.setPar(updatedHole.getPar());

    producer.holeUpdated(payload(hole));

    return hole;
  }

  void delete(long courseId, int holeNumber) {
    repository.findByCourseIdAndNumber(courseId, holeNumber)
        .ifPresent(hole -> {
          repository.deleteById(hole.getId());
          producer.holeDeleted(payload(hole));
        });
  }

  private static HoleEntity findHoleOrThrow(CourseEntity course, int holeNumber) {
    return course.findHole(holeNumber)
        .orElseThrow(() -> {
          String msg = String.format("Course %s has no hole %s", course.getId(), holeNumber);
          return new NotFoundException(msg);
        });
  }

  private static HolePayload payload(HoleEntity entity) {
    return new HolePayload(
        entity.getId(),
        entity.getCourse().getId(),
        entity.getHoleNumber(),
        entity.getPar(),
        entity.getDistance());
  }

}
