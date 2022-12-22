package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class ScoreCardRepository implements PanacheRepository<ScoreCardEntity> {
}
