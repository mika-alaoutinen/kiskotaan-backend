package mikaa.feature.course;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
  @GeneratedValue
  @Column(name = "course_id")
  private long courseId;

  private Long id;
  private int holes;
  private String name;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course", orphanRemoval = true)
  private Set<ScoreCardEntity> scorecards = new HashSet<>();

  public CourseEntity(long id, int holes, String name) {
    this.id = id;
    this.holes = holes;
    this.name = name;
  }

  CourseEntity incrementHoleCount() {
    holes++;
    return this;
  }

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
