package mikaa.feature.scorecard;

import java.util.ArrayList;
import java.util.List;

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
  private List<Hole> holes = new ArrayList<>();

  public void addHole(Hole hole) {
    holes.add(hole);
  }

  public void removeHole(Hole hole) {
    holes.remove(hole);
  }

}
