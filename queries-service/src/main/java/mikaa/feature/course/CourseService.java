package mikaa.feature.course;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.Hole;
import mikaa.consumers.course.CourseWriter;
import mikaa.queries.course.CourseReader;
import mikaa.uni.UniDecorator;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseReader, CourseWriter {

  private final CourseRepository repository;

  @Override
  public Uni<CourseEntity> findOne(long externalId) {
    return UniDecorator
        .from(repository.findByExternalId(externalId))
        .orThrow(new NotFoundException("Could not find course with ID " + externalId));
  }

  @Override
  public Multi<CourseEntity> findAll() {
    return repository.streamAll();
  }

  @Override
  public Uni<CourseEntity> add(CoursePayload payload) {
    return repository.persist(toCourse(payload));
  }

  @Override
  public Uni<CourseEntity> update(CoursePayload payload) {
    return UniDecorator
        .from(repository.findByExternalId(payload.getId()))
        .map(course -> {
          course.setName(payload.getName());
          return course;
        })
        .flatMap(repository::update)
        .unwrap();
  }

  @Override
  public Uni<Void> delete(CoursePayload payload) {
    return UniDecorator
        .from(repository.findByExternalId(payload.getId()))
        .ifPresent(repository::delete);
  }

  private static CourseEntity toCourse(CoursePayload payload) {
    var holes = payload.getHoles().stream().map(CourseService::hole).toList();
    return new CourseEntity(payload.getId(), payload.getName(), holes);
  }

  private static HoleEntity hole(Hole payload) {
    return new HoleEntity(
        payload.getId(),
        payload.getNumber(),
        payload.getPar(),
        payload.getDistance());
  }

}
