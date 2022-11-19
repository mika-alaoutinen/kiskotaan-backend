package mikaa.feature;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mikaa.dto.HoleDTO;
import mikaa.dto.NewHoleDTO;

@EqualsAndHashCode(callSuper = false)
@ToString
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

  public HoleEntity(int holeNumber, int par, int distance) {
    this.holeNumber = holeNumber;
    this.par = par;
    this.distance = distance;
  }

  public HoleEntity(HoleDTO hole) {
    this(hole.number(), hole.par(), hole.distance());
    this.id = hole.id();
  }

  public HoleEntity(NewHoleDTO newHole) {
    this(newHole.number(), newHole.par(), newHole.distance());
  }

  // For tests
  public HoleEntity(long id, int holeNumber, int par, int distance) {
    this.id = id;
    this.holeNumber = holeNumber;
    this.par = par;
    this.distance = distance;
  }

}
