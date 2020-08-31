package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Score extends EntityModel {

    @NotNull
    @Min(1)
    private Long playerId;

    @Min(1)
    @Max(99)
    private int score;

    // Hibernate
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scoreRow_id")
    private ScoreRow scoreRow;

    public Score(Long playerId, int score) {
        this.playerId = playerId;
        this.score = score;
    }
}
