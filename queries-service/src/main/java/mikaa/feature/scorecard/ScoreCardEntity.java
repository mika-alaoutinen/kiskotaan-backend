package mikaa.feature.scorecard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@MongoEntity(collection = "scorecard")
public class ScoreCardEntity {

  private ObjectId id;
  private long externalId;
  private Course course;
  private List<Player> players = new ArrayList<>();

  public ScoreCardEntity(long externalId) {
    this.externalId = externalId;
  }

  public Player addPlayer(Player player) {
    players.add(player);
    return player;
  }

  public void removePlayer(Player player) {
    players.remove(player);
  }

  public Score addScore(long playerId, Score score) {
    findPlayer(playerId).ifPresent(p -> p.addScore(score));
    return score;
  }

  public void removeScore(long playerId, Score score) {
    findPlayer(playerId).ifPresent(p -> p.removeScore(score));
  }

  public Optional<Player> findPlayer(long playerId) {
    return players.stream()
        .filter(p -> p.getExternalId() == playerId)
        .findFirst();
  }

}
