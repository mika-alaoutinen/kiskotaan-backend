package com.mika.kiskotaan.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Course extends EntityModel {

    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @NotNull
    @Size(min = 1, max = 30)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
    private List<Hole> holes = new ArrayList<>();

    // Hibernate
    @OneToOne
    @JoinColumn(name = "scoreCard_id", referencedColumnName = "id")
    private ScoreCard scoreCard;

    public Course(String name, List<Hole> holes) {
        this.name = name;
        this.holes = holes;
    }
}
