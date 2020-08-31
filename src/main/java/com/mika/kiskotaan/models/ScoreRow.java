package com.mika.kiskotaan.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
//@AllArgsConstructor
public class ScoreRow extends EntityModel {

    @NotNull
    @Min(1)
    @Max(30)
    private int hole;

    @OneToMany
    private List<Score> scores;

    // Needed for Hibernate to work:
    @ManyToOne(fetch = FetchType.LAZY)
    private ScoreCard scoreCard;

    public ScoreRow(int hole, List<Score> scores) {
        this.hole = hole;
        this.scores = scores;
    }

    public void addScore(Score score) {
        this.scores.add(score);
        score.setScoreRow(this);
    }

    public void removeScore(Score score) {
        this.scores.remove(score);
        score.setScoreRow(null);
    }
}
