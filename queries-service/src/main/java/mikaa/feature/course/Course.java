package mikaa.feature.course;

import java.util.List;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, exclude = "holes")
@ToString(exclude = "holes")
@MongoEntity
public class Course {

  private ObjectId id;
  private long externalId;
  private String name;
  private List<Hole> holes;

}
