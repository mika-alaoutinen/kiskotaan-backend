package mikaa.feature;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
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
public class CourseEntity extends PanacheEntity {

  @NotBlank(message = "Course name is required")
  @Size(min = 3, max = 40, message = "Course name must be 3-40 chars long")
  @Column(nullable = false)
  public String name;

  @Size(min = 1, max = 30, message = "Course can haven 1-30 holes")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
  public List<HoleEntity> holes;

  public void addHole(HoleEntity hole) {
    holes.add(hole);
    hole.setCourse(this);
  }

  public void removeHole(HoleEntity hole) {
    holes.remove(hole);
    hole.setCourse(null);
  }

  public static Optional<CourseEntity> findByName(String name) {
    return find("name", name).firstResultOptional();
  }
}
