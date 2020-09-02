package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Game extends EntityModel {

    private boolean hasScoreChanged;
    private boolean isOver;

    @Min(1)
    @Max(30)
    private int hole;

    @OneToOne(mappedBy = "game")
    private ScoreCard scoreCard;

    public Game(boolean hasScoreChanged, boolean isOver, int hole) {
        this.hasScoreChanged = hasScoreChanged;
        this.isOver = isOver;
        this.hole = hole;
    }
}
