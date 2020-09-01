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
    @OneToOne(cascade = CascadeType.MERGE, orphanRemoval = true)
    private Course course;

    @NotNull
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    @NotNull
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ScoreRow> rows = new ArrayList<>();
}
