package mikaa.feature;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
class HoleRepository implements PanacheRepository<HoleEntity> {
}
