package mikaa.feature.scorecard;

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

  public ScoreCardEntity(long externalId) {
    this.externalId = externalId;
  }

}
