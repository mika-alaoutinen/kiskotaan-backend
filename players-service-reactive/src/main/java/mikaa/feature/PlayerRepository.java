package mikaa.feature;

import java.util.List;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class PlayerRepository implements PanacheRepository<PlayerEntity> {

  Uni<PlayerEntity> deleteById(long id) {
    return findById(id)
        .onItem()
        .ifNotNull()
        .call(this::delete);
  }

  Uni<Boolean> existsByFirstNameAndLastName(String firstName, String lastName) {
    return findByFirstAndLastname(firstName, lastName).map(list -> !list.isEmpty());
  }

  Uni<List<PlayerEntity>> findByFirstAndLastname(String firstName, String lastName) {
    return list("lower(firstName) LIKE CONCAT('%', ?1, '%') and lower(lastName) LIKE CONCAT('%', ?2, '%')",
        firstName.toLowerCase(), lastName.toLowerCase());
  }

  Uni<List<PlayerEntity>> findByFirstOrLastname(String name) {
    String query = """
        SELECT p FROM player p WHERE
          LOWER(p.firstName) LIKE LOWER(CONCAT('%', ?1, '%')) OR
          LOWER(p.lastName) LIKE LOWER(CONCAT('%', ?1, '%'))
          """;

    return list(query, name);
  }

}
