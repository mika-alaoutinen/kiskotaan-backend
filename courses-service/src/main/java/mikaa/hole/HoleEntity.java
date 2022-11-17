package mikaa.hole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mikaa.course.CourseEntity;

@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "hole")
public class HoleEntity extends PanacheEntity {

  @Min(1)
  @Max(30)
  @Column(name = "hole_number")
  public int holeNumber;

  @Min(2)
  @Max(6)
  @Column
  public int par;

  @Min(1)
  @Column
  public int distance;

  @ManyToOne(fetch = FetchType.LAZY)
  public CourseEntity course;

}
