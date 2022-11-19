package mikaa.feature;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class CourseEntity {

  @Id
  @GeneratedValue
  private long id;

  @NotBlank(message = "Course name is required")
  @Size(min = 3, max = 40, message = "Course name must be 3-40 chars long")
  @Column(nullable = false)
  private String name;

  @Size(min = 1, max = 30, message = "Course can haven 1-30 holes")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
  private List<HoleEntity> holes = new ArrayList<>();

  CourseEntity(String name) {
    this.name = name;
    this.holes = new ArrayList<>();
  }

  public void addHole(HoleEntity hole) {
    holes.add(hole);
    hole.setCourse(this);
  }

  public void removeHole(HoleEntity hole) {
    holes.remove(hole);
    hole.setCourse(null);
  }

}
