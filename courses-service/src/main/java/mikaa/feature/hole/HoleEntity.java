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

  HoleEntity(int number, int par, int distance) {
    this.number = number;
    this.par = par;
    this.distance = distance;
  }

  public static HoleEntity from(int holeNumber, int par, int distance) {
    return new HoleEntity(null, holeNumber, par, distance, null);
  }

  /**
   * Write default getter for ID, because Panache repository's persist method
   * returns void. Therefore, a mocked repository will return null for an ID even
   * though that does not happen in reality, and this is difficult to fix.
   * 
   * Thanks for this feature, I guess?
   * 
   * @return id or default value of "-1"
   */
  long getId() {
    return id != null ? id : -1;
  }

}
