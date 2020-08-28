package com.mika.kiskotaan.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ScoreCard extends EntityModel {

    @NotNull
    @OneToOne
    private Course course;

    @OneToMany
    private List<Player> players;

    @OneToMany
    private Map<Integer, ScoreRow> scoreRows;
}
