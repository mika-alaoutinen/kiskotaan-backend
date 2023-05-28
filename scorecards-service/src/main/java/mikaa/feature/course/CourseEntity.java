package mikaa.feature.course;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mikaa.feature.scorecard.ScoreCardEntity;

@Data
@EqualsAndHashCode(callSuper = false, exclude = "scorecards")
@ToString(exclude = "scorecards")
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "course")
public class CourseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter(value = AccessLevel.PACKAGE)
  @Setter(value = AccessLevel.PACKAGE)
  private Long id;

  @Column(name = "external_id", nullable = false, unique = true)
  private long externalId;

  private int holes;
  private String name;
  private int par;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
  private Set<ScoreCardEntity> scorecards = new HashSet<>();

  public CourseEntity(long externalId, int holes, String name, int par) {
    this.externalId = externalId;
    this.holes = holes;
    this.name = name;
    this.par = par;
  }

  CourseEntity addHole(int par) {
    this.holes++;
    this.par += par;
    return this;
  }

  CourseEntity removeHole(int par) {
    this.holes--;
    this.par -= par;
    return this;
  }

  // del
  CourseEntity incrementHoleCount() {
    holes++;
    return this;
  }

  // del
  CourseEntity decrementHoleCount() {
    holes--;
    return this;
  }

  void addScoreCard(ScoreCardEntity scoreCard) {
    scorecards.add(scoreCard);
    scoreCard.setCourse(this);
  }

  void removeScoreCard(ScoreCardEntity scoreCard) {
    scorecards.remove(scoreCard);
    scoreCard.setCourse(null);
  }

}
