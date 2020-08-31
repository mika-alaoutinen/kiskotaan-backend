package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class ScoreRow extends EntityModel {

    @NotNull
    @Min(1)
    @Max(30)
    private int hole;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "scoreRow", orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

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
