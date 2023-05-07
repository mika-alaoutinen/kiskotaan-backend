package mikaa.feature.course;

import java.util.Optional;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class CourseRepository implements ReactivePanacheMongoRepository<Course> {

  Uni<Optional<Course>> findByExternalId(long externalId) {
    return find("externalId", externalId).firstResultOptional();
  }

}
