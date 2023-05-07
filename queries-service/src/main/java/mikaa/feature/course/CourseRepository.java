package mikaa.feature.course;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class CourseRepository implements ReactivePanacheMongoRepository<Course> {

  Uni<Course> findByExternalId(long externalId) {
    return find("externalId", externalId).firstResult();
  }

}
