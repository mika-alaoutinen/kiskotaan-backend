package mikaa.feature.course;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "id", "courseId" })
@MongoEntity
public class Hole {

  @Getter(value = AccessLevel.PACKAGE)
  @Setter(value = AccessLevel.PACKAGE)
  private ObjectId id;
  private long externalId;
  private int holeNumber;
  private int par;
  private int distance;
  private String courseId;

}
