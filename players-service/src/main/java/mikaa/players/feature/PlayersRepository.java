package mikaa.players.feature;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface PlayersRepository extends JpaRepository<PlayerEntity, Long> {

  boolean existsPlayerByFirstNameAndLastName(String firstName, String lastName);

  List<PlayerEntity> findByFirstNameContainingAndLastNameContaining(String firstName, String lastName);

  @Query("SELECT p FROM player p WHERE p.firstName LIKE :name OR p.lastName LIKE :name")
  List<PlayerEntity> findByFirstOrLastname(@Param("name") String name);

}
