package mikaa.feature.course;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "course_holes", joinColumns = @JoinColumn(name = "course_id"))
  private List<HoleEntity> holes = new ArrayList<>();

  private String name;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
  private Set<ScoreCardEntity> scorecards = new HashSet<>();

  public CourseEntity(long externalId, String name) {
    this.externalId = externalId;
    this.name = name;
  }

  public CourseEntity(long externalId, List<HoleEntity> holes, String name) {
    this.externalId = externalId;
    this.holes = holes;
    this.name = name;
  }

  CourseEntity addHole(HoleEntity hole) {
    holes.add(hole);
    return this;
  }

  CourseEntity removeHole(int number) {
    holes = holes.stream().filter(h -> h.getNumber() != number).toList();
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
