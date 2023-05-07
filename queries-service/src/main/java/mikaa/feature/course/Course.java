package mikaa.feature.course;

import java.util.List;

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
@ToString(exclude = "id")
@MongoEntity(collection = "course")
public class Course {

  @Getter(value = AccessLevel.PACKAGE)
  @Setter(value = AccessLevel.PACKAGE)
  private ObjectId id;
  private long externalId;
  private String name;
  private List<Hole> holes;

  public Course(long externalId, String name, List<Hole> holes) {
    this.externalId = externalId;
    this.name = name;
    this.holes = List.copyOf(holes);
  }

}
