package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Hole extends EntityModel {

    @NotNull
    @Min(1)
    @Max(30)
    private int number;

    @NotNull
    @Min(1)
    @Max(6)
    private int par;

    private int distance;
}
