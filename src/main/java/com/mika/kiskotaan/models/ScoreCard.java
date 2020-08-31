package com.mika.kiskotaan.models;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ScoreCard extends EntityModel {

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "scoreCard", orphanRemoval = true)
    private List<Player> players;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "scoreCard", orphanRemoval = true)
    private List<ScoreRow> rows;
}
