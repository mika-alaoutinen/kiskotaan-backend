package mikaa.feature.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class HoleEntity {

  private long externalId;
  private int holeNumber;
  private int par;
  private int distance;

}
