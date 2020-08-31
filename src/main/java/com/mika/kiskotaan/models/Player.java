package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Player extends EntityModel {

    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    // Hibernate
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    private ScoreCard scoreCard;

    public Player(String name) {
        this.name = name;
    }
}