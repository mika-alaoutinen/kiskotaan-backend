package mikaa.feature.score;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class ScoreRepository implements PanacheRepository<ScoreEntity> {
}
