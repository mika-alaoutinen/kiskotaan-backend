package mikaa.feature.course;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, exclude = "courseId")
@ToString(exclude = "courseId")
@MongoEntity
public class Hole {

  private ObjectId id;
  private long externalId;
  private int holeNumber;
  private int par;
  private int distance;
  private String courseId;

}
