package mikaa.feature;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "course")
class CourseEntity {

  @Id
  @GeneratedValue
  private Long id;

  private int holes;

  @OneToOne
  @MapsId
  @JoinColumn(name = "scorecard_id")
  private ScoreCardEntity scorecard;

}
