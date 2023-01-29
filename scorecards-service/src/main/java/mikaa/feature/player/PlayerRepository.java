package mikaa.feature.player;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class PlayerRepository implements PanacheRepository<PlayerEntity> {

  Optional<PlayerEntity> findByExternalId(long externalId) {
    return find("externalId", externalId).firstResultOptional();
  }

}
