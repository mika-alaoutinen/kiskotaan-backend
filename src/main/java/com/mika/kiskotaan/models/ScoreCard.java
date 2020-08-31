package com.mika.kiskotaan.models;

import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class ScoreCard extends EntityModel {

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scoreCard", orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scoreCard", orphanRemoval = true)
    private List<ScoreRow> rows = new ArrayList<>();

    public void addPlayer(Player player) {
        this.players.add(player);
        player.setScoreCard(this);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
        player.setScoreCard(null);
    }

    public void addRow(ScoreRow row) {
        this.rows.add(row);
        row.setScoreCard(this);
    }

    public void removeRow(ScoreRow row) {
        this.rows.remove(row);
        row.setScoreCard(null);
    }
}
