package mikaa.feature;

import java.util.List;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class PlayerRepository implements PanacheRepository<PlayerEntity> {

  Uni<Boolean> existsByFirstNameAndLastName(String firstName, String lastName) {
    return findByFirstAndLastname(firstName, lastName).map(list -> !list.isEmpty());
  }

  Uni<List<PlayerEntity>> findByFirstAndLastname(String firstName, String lastName) {
    return list("lower(firstName) = ?1 and lower(lastName) = ?2",
        firstName.toLowerCase(), lastName.toLowerCase());
  }

  Uni<List<PlayerEntity>> findByFirstOrLastname(String name) {
    String nameQuery = "%" + name.toLowerCase() + "%";
    return list("firstName like ?1 or lastName like ?1", nameQuery);
  }

}
