package mikaa.players.feature;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PlayersRepository extends JpaRepository<PlayerEntity, Long> {
}
