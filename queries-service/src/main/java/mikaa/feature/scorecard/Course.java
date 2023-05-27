package mikaa.feature.scorecard;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Course {

  private long externalId;
  private String name;
  private Set<Hole> holes = new HashSet<>();

  public void addHole(Hole hole) {
    holes.add(hole);
  }

  public void removeHole(Hole hole) {
    holes.remove(hole);
  }

}
