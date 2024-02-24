package mikaa.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, exclude = "holes")
@ToString(exclude = "holes")
@Entity(name = "course")
class CourseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Course name is required")
  @Size(min = 3, max = 40, message = "Course name must be 3-40 chars long")
  @Column(nullable = false, unique = true)
  private String name;

  @Size(min = 1, max = 30, message = "Course can haven 1-30 holes")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
  private List<HoleEntity> holes = new ArrayList<>();

  CourseEntity(String name, List<HoleEntity> holes) {
    this.name = name;
    this.holes = holes;
  }

  long getId() {
    return id != null ? id : -1;
  }

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

  Optional<HoleEntity> findHole(int number) {
    return this.getHoles()
        .stream()
        .filter(h -> h.getNumber() == number)
        .findFirst();
  }

}
