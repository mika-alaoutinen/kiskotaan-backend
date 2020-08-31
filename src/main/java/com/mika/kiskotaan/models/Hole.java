package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Hole extends EntityModel {

    @NotNull
    @Min(1)
    @Max(30)
    private int number;

    @NotNull
    @Min(1)
    @Max(6)
    private int par;

    @Min(1)
    @Max(1000)
    private int distance; // in meters

    // Hibernate
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "course_id")
//    private Course course;

    public Hole(int number, int par) {
        this.number = number;
        this.par = par;
    }

    public Hole(int number, int par, int distance) {
        this.number = number;
        this.par = par;
        this.distance = distance;
    }
}
