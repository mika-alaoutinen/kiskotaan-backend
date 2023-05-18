package mikaa.feature;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;

/**
 * A base repository class with operations needed by all other repositories.
 */
public abstract class KiskotaanRepository<T> implements ReactivePanacheMongoRepository<T> {

  public Uni<T> findByExternalId(long externalId) {
    return find("externalId", externalId).firstResult();
  }

}
