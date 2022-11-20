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
class CourseEntity {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank(message = "Course name is required")
  @Size(min = 3, max = 40, message = "Course name must be 3-40 chars long")
  @Column(nullable = false)
  private String name;

  @Size(min = 1, max = 30, message = "Course can haven 1-30 holes")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
  private List<HoleEntity> holes = new ArrayList<>();

  static CourseEntity fromName(String name) {
    return new CourseEntity(null, name, new ArrayList<>());
  }

  void addHole(HoleEntity hole) {
    holes.add(hole);
    hole.setCourse(this);
  }

  void removeHole(HoleEntity hole) {
    holes.remove(hole);
    hole.setCourse(null);
  }

}
