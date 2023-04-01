package mikaa.feature.player;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class PlayerRepository implements PanacheRepository<PlayerEntity> {

  void deleteByExternalId(long externalId) {
    findByExternalId(externalId).ifPresent(this::delete);
  }

  Optional<PlayerEntity> findByExternalId(long externalId) {
    return find("externalId", externalId).firstResultOptional();
  }

}
