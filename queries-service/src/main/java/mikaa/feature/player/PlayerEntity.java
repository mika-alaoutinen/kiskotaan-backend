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

  public PlayerEntity(long externalId, String firstName, String lastName) {
    this.externalId = externalId;
    this.firstName = firstName;
    this.lastName = lastName;
  }

}
