package mikaa.feature.player;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@MongoEntity(collection = "player")
public class PlayerEntity {

  private ObjectId id;
  private long externalId;
  private String firstName;
  private String lastName;

}
