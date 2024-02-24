package mikaa.feature.hole;

import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.domain.Hole;
import mikaa.domain.NewHole;
import mikaa.domain.UpdatedHole;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.CourseFinder;
import mikaa.kiskotaan.course.HolePayload;
import mikaa.producers.holes.HoleProducer;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseFinder courseFinder;
  private final HoleProducer producer;
  private final HoleRepository repository;

  List<Hole> findHoles(long courseId) {
    var holes = courseFinder.findCourse(courseId)
        .map(CourseEntity::getHoles)
        .orElseGet(Collections::emptyList);

    return holes.stream().map(HoleService::toHole).toList();
  }

  Hole findOne(long courseId, int holeNumber) {
    var course = courseFinder.findCourseOrThrow(courseId);
    var hole = findHoleOrThrow(course, holeNumber);
    return toHole(hole);
  }

  Hole add(long courseId, NewHole newHole) {
    var course = courseFinder.findCourseOrThrow(courseId);
    HoleValidator.validateUniqueHoleNumber(newHole.number(), course);

    var holeEntity = new HoleEntity(newHole.number(), newHole.par(), newHole.distance());
    course.addHole(holeEntity);

    repository.persist(holeEntity);
    producer.holeAdded(payload(holeEntity));

    return toHole(holeEntity);
  }

  Hole update(long courseId, int holeNumber, UpdatedHole updatedHole) {
    var course = courseFinder.findCourseOrThrow(courseId);
    var hole = findHoleOrThrow(course, holeNumber);

    hole.setDistance(updatedHole.distance());
    hole.setPar(updatedHole.par());

    producer.holeUpdated(payload(hole));

    return toHole(hole);
  }

  void delete(long courseId, int holeNumber) {
    repository.findByCourseIdAndNumber(courseId, holeNumber)
        .map(HoleService::payload)
        .ifPresent(payload -> {
          repository.deleteById(payload.getId());
          producer.holeDeleted(payload);
        });
  }

  private static HoleEntity findHoleOrThrow(CourseEntity course, int holeNumber) {
    return course.findHole(holeNumber).orElseThrow(
        () -> new NotFoundException(String.format("Course %s has no hole %s", course.getId(), holeNumber)));
  }

  private static HolePayload payload(HoleEntity entity) {
    return new HolePayload(
        entity.getId(),
        entity.getCourse().getId(),
        entity.getNumber(),
        entity.getPar(),
        entity.getDistance());
  }

  private static Hole toHole(HoleEntity entity) {
    return new Hole(entity.getId(), entity.getNumber(), entity.getPar(), entity.getDistance());
  }

}
