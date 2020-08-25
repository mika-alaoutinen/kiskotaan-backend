package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Score extends EntityModel {

    @NotNull
    @ManyToOne
    private Player player;

    @Min(1)
    @Max(99)
    private int score;
}
