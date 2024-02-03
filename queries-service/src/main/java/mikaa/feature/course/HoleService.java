package mikaa.feature.course;

import java.util.stream.Stream;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.courses.HolePayload;
import mikaa.consumers.course.HoleWriter;
import mikaa.queries.course.CourseReader;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService implements HoleWriter {

  private final CourseReader reader;
  private final CourseRepository repository;

  @Override
  public Uni<HoleEntity> add(HolePayload payload) {
    var hole = toHole(payload);

    return reader.findOne(payload.getCourseId())
        .map(course -> addHole(course, hole))
        .flatMap(repository::update)
        .chain(() -> Uni.createFrom().item(hole));
  }

  @Override
  public Uni<HoleEntity> update(HolePayload payload) {
    var hole = toHole(payload);

    return reader.findOne(payload.getCourseId())
        .map(course -> updateHole(course, hole))
        .flatMap(repository::update)
        .chain(() -> Uni.createFrom().item(hole));
  }

  @Override
  public Uni<Void> delete(HolePayload payload) {
    return reader.findOne(payload.getCourseId())
        .map(course -> deleteHole(course, payload.getId()))
        .flatMap(repository::update)
        .chain(() -> Uni.createFrom().nullItem());
  }

  private static CourseEntity addHole(CourseEntity course, HoleEntity newHole) {
    var holes = Stream.concat(course.getHoles().stream(), Stream.of(newHole)).toList();
    course.setHoles(holes);
    return course;
  }

  private static CourseEntity updateHole(CourseEntity course, HoleEntity updated) {
    var holes = course.getHoles()
        .stream()
        .map(hole -> hole.getExternalId() == updated.getExternalId() ? updated : hole)
        .toList();

    course.setHoles(holes);
    return course;
  }

  private static CourseEntity deleteHole(CourseEntity course, Long holeId) {
    var holes = course.getHoles()
        .stream()
        .filter(hole -> hole.getExternalId() != holeId)
        .toList();

    course.setHoles(holes);
    return course;
  }

  private static HoleEntity toHole(HolePayload payload) {
    return new HoleEntity(
        payload.getId(),
        payload.getNumber(),
        payload.getPar(),
        payload.getDistance());
  }

}
