package mikaa.feature.scorecard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Hole {

  private long externalId;
  private int number;
  private int distance;
  private int par;

}
