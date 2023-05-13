package mikaa.feature.course;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import mikaa.HolePayload;
import mikaa.consumers.course.HoleWriter;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService implements HoleWriter {

  private final CourseRepository repository;

  @Override
  public Uni<HoleEntity> add(HolePayload payload) {
    var hole = toHole(payload);

    // sort holes?
    return repository.findByExternalId(payload.courseId())
        .onItem()
        .ifNull()
        .failWith(() -> notFound(payload.courseId()))
        .map(course -> {
          course.getHoles().add(hole);
          return course;
        })
        .flatMap(repository::update)
        .chain(() -> Uni.createFrom().item(hole));
  }

  @Override
  public Uni<HoleEntity> update(HolePayload payload) {
    return Uni.createFrom().nothing();
  }

  @Override
  public Uni<Void> delete(HolePayload payload) {
    return Uni.createFrom().nothing();
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with ID " + id);
  }

  private static HoleEntity toHole(HolePayload payload) {
    return new HoleEntity(
        payload.id(),
        payload.number(),
        payload.par(),
        payload.distance());
  }

}
