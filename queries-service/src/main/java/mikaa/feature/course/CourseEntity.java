package mikaa.feature.course;

import java.util.List;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false, exclude = "holes")
@NoArgsConstructor
@MongoEntity(collection = "course")
public class CourseEntity {

  private ObjectId id;
  private long externalId;
  private String name;
  private List<HoleEntity> holes;

  public CourseEntity(long externalId, String name, List<HoleEntity> holes) {
    this.externalId = externalId;
    this.name = name;
    this.holes = List.copyOf(holes);
  }

}
