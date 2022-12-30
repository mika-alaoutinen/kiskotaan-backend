package mikaa.feature.player;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class PlayerRepository implements PanacheRepository<PlayerEntity> {
}
