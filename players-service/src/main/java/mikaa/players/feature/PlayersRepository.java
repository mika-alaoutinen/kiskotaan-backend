package mikaa.players.feature;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface PlayersRepository extends JpaRepository<PlayerEntity, Long> {

  boolean existsPlayerByFirstNameAndLastName(String firstName, String lastName);

  List<PlayerEntity> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
      String firstName, String lastName);

  @Query("SELECT p FROM player p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
  List<PlayerEntity> findByFirstOrLastname(@Param("name") String name);

}
