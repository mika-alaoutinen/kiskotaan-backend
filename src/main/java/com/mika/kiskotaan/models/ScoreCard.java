package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class ScoreCard extends EntityModel {

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "scoreCard", orphanRemoval = true)
    private Course course;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "scoreCard", orphanRemoval = true)
    private List<Player> players;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "scoreCard", orphanRemoval = true)
    private List<ScoreRow> rows;

    // Hibernate
    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public ScoreCard(Course course, List<Player> players, List<ScoreRow> rows) {
        this.course = course;
        this.players = players;
        this.rows = rows;
    }
}
