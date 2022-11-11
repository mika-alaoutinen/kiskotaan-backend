package mikaa.players.feature;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@ToString
class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(min = 1, max = 40)
  @Column(nullable = false)
  private String firstName;

  @Size(min = 1, max = 40)
  @Column(nullable = false)
  private String lastName;

  Player(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
