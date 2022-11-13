package mikaa.players.feature;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity(name = "player")
@AllArgsConstructor
@NoArgsConstructor
@ToString
class PlayerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(min = 1, max = 40)
  @Column(nullable = false)
  private String firstName;

  @Size(min = 1, max = 40)
  @Column(nullable = false)
  private String lastName;

  PlayerEntity(String firstName, String lastName) {
    this.firstName = Objects.requireNonNull(firstName);
    this.lastName = Objects.requireNonNull(lastName);
  }
}
