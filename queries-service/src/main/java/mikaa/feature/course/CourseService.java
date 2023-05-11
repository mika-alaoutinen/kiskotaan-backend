package mikaa.feature.course;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import mikaa.CoursePayload;
import mikaa.CourseUpdated;
import mikaa.Hole;
import mikaa.consumers.course.CourseWriter;
import mikaa.queries.course.CourseReader;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseReader, CourseWriter {

  private final CourseRepository repository;

  @Override
  public Uni<CourseEntity> findOne(long externalId) {
    return repository.findByExternalId(externalId)
        .replaceIfNullWith(() -> {
          throw notFound(externalId);
        });
  }

  @Override
  public Multi<CourseEntity> findAll() {
    return repository.streamAll();
  }

  @Override
  public void add(CoursePayload payload) {
    repository.persist(toCourse(payload));
  }

  @Override
  public void update(CourseUpdated payload) {
    repository.findByExternalId(payload.id())
        .onItem()
        .ifNotNull()
        .transform(course -> {
          course.setName(payload.name());
          return course;
        });
  }

  @Override
  public void delete(CoursePayload payload) {
    repository.findByExternalId(payload.id())
        .onItem()
        .ifNotNull()
        .invoke(repository::delete);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with ID " + id);
  }

  private static CourseEntity toCourse(CoursePayload payload) {
    var holes = payload.holes().stream().map(CourseService::hole).toList();
    return new CourseEntity(payload.id(), payload.name(), holes);
  }

  private static HoleEntity hole(Hole payload) {
    return new HoleEntity(payload.id(), payload.number(), payload.par(), payload.distance());
  }

}
