package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
@NoArgsConstructor
public class ScoreCard extends EntityModel {

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "scoreCard", orphanRemoval = true)
    private Course course;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scoreCard", orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scoreCard", orphanRemoval = true)
    private List<ScoreRow> rows = new ArrayList<>();

    // Hibernate
    public ScoreCard(Course course, List<Player> players, List<ScoreRow> rows) {
        this.course = course;
        this.players = players;
        this.rows = rows;
    }

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public void addPlayer(Player player) {
        this.players.add(player);
        player.setScoreCard(this);
    }

    public void addPlayers(List<Player> players) {
        this.players.addAll(players);
        players.forEach(p -> p.setScoreCard(this));
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
        player.setScoreCard(null);
    }

    public void addScoreRow(ScoreRow row) {
        this.rows.add(row);
        row.setScoreCard(this);
    }

    public void addScoreRows(List<ScoreRow> rows) {
        this.rows.addAll(rows);
        rows.forEach(r -> r.setScoreCard(this));
    }

    public void removeScoreRow(ScoreRow row) {
        this.rows.remove(row);
        row.setScoreCard(null);
    }
}
