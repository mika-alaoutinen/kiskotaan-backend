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
    String query = "LOWER(firstName) LIKE ?1 and LOWER(lastName) LIKE ?2";
    return list(query, like(firstName), like(lastName));
  }

  Uni<List<PlayerEntity>> findByFirstOrLastname(String name) {
    String query = "LOWER(firstName) LIKE ?1 OR LOWER(lastName) LIKE ?1";
    return list(query, like(name));
  }

  private static String like(String s) {
    return "%" + s.toLowerCase() + "%";
  }

}
