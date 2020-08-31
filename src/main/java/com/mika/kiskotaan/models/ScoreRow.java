package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    private List<Score> scores;

    // Hibernate
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scoreCard_id")
    private ScoreCard scoreCard;

    public ScoreRow(int hole, List<Score> scores) {
        this.hole = hole;
        this.scores = scores;
    }
}
