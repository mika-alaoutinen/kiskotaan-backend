package mikaa.feature;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
@Entity(name = "hole")
class HoleEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Min(1)
  @Max(30)
  @Column(name = "hole_number")
  private int holeNumber;

  @Min(2)
  @Max(6)
  @Column
  private int par;

  @Min(1)
  @Column
  private int distance;

  @ManyToOne(fetch = FetchType.LAZY)
  private CourseEntity course;

  static HoleEntity from(int holeNumber, int par, int distance) {
    return new HoleEntity(null, holeNumber, par, distance, null);
  }

}
