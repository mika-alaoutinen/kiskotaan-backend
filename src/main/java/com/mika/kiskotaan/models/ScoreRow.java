package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ScoreRow extends EntityModel {

    @NotNull
    @Min(1)
    @Max(30)
    private int hole;

    @OneToMany
    private List<Score> scores;
}
