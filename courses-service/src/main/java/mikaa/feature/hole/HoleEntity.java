package mikaa.feature.hole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mikaa.feature.course.CourseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, exclude = "course")
@ToString(exclude = "course")
@Entity(name = "hole")
public class HoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Min(1)
  @Max(30)
  @Column(name = "number")
  private int number;

  @Min(2)
  @Max(6)
  @Column
  private int par;

  @Min(1)
  @Column
  private int distance;

  @ManyToOne(fetch = FetchType.LAZY)
  private CourseEntity course;

  public static HoleEntity from(int holeNumber, int par, int distance) {
    return new HoleEntity(null, holeNumber, par, distance, null);
  }

}